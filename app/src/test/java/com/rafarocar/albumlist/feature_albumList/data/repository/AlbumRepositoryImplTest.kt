package com.rafarocar.albumlist.feature_albumList.data.repository

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rafarocar.albumlist.feature_albumList.common.AlbumDiffUtilForTests
import com.rafarocar.albumlist.feature_albumList.common.NoopListCallback
import com.rafarocar.albumlist.feature_albumList.data.local.AlbumDatabase
import com.rafarocar.albumlist.feature_albumList.data.local.AlbumEntity
import com.rafarocar.albumlist.feature_albumList.data.mapper.toDomain
import com.rafarocar.albumlist.feature_albumList.data.mapper.toEntity
import com.rafarocar.albumlist.feature_albumList.data.remote.AlbumApi
import com.rafarocar.albumlist.feature_albumList.data.remote.AlbumDTO
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AlbumRepositoryImplTest {

    private val mockedApi = mockk<AlbumApi>()
    private val mockedDatabase = mockk<AlbumDatabase>()
    lateinit var repositoryImpl: AlbumRepositoryImpl
    private val dispatcher = StandardTestDispatcher()

    // Simple PagingSource that just returns a list
    val fakePagingSource = object : PagingSource<Int, AlbumEntity>() {
        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AlbumEntity> {
            val items = listOf(
                AlbumEntity(
                    id = 1,
                    albumId = 1,
                    title = "Album 1",
                    url = "url1",
                    thumbnailURL = "thumb1"
                ),
                AlbumEntity(
                    id = 2,
                    albumId = 1,
                    title = "Album 2",
                    url = "url2",
                    thumbnailURL = "thumb2"
                )
            )
            return LoadResult.Page(
                data = items,
                prevKey = null,
                nextKey = null
            )
        }

        override fun getRefreshKey(state: PagingState<Int, AlbumEntity>): Int? = null
    }

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        repositoryImpl = AlbumRepositoryImpl(mockedApi, mockedDatabase)
        mockkStatic(android.util.Log::class)
        every { android.util.Log.i(any(), any()) } returns 0
        every { android.util.Log.d(any(), any()) } returns 0
        every { android.util.Log.e(any(), any()) } returns 0
        every { android.util.Log.e(any(), any(), any()) } returns 0
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `getAlbums return a Flow PagingData Album`() = runTest {
        // Arrange
        val fakeEntities = listOf(
            AlbumEntity(1, 1, "Album 1", "url1", "thumb1"),
            AlbumEntity(2, 1, "Album 2", "url2", "thumb2")
        )
        val fakeAlbums = fakeEntities.map { it.toDomain() }

        val pagingSource = fakePagingSource
        every { mockedDatabase.albumDao().getAlbumsPaging() } returns pagingSource

        val differ = AsyncPagingDataDiffer(
            diffCallback = AlbumDiffUtilForTests,
            updateCallback = NoopListCallback(),
            workerDispatcher = dispatcher
        )

        // Act
        val flow = repositoryImpl.getAlbums()
        val job = launch {
            flow.collect { pagingData ->
                differ.submitData(pagingData)
            }
        }

        advanceUntilIdle()

        // Assert
        val snapshot = differ.snapshot().items
        assertEquals(fakeAlbums.size, snapshot.size)
        assertEquals(fakeAlbums[0], snapshot[0])
        assertEquals(fakeAlbums[1], snapshot[1])

        job.cancel()

    }

    @Test
    fun `refreshAlbums calls insertAlbums in database`() = runTest {
        val fakesDao = listOf(
            AlbumDTO(1, 1, "Album 1", "url1", "thumb1"),
            AlbumDTO(2, 1, "Album 2", "url2", "thumb2")
        )

        coEvery { mockedApi.getAlbums() } returns fakesDao
        coEvery { mockedDatabase.albumDao().insertAlbums(any()) } returns Unit
        repositoryImpl.refreshAlbums()
        coVerify(exactly = 1) {
            mockedDatabase.albumDao().insertAlbums(fakesDao.map { it.toEntity() })
        }
    }
}