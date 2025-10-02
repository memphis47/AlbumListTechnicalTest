package com.rafarocar.albumlist.feature_albumList.domain.useCase

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.rafarocar.albumlist.feature_albumList.common.AlbumDiffUtilForTests
import com.rafarocar.albumlist.feature_albumList.common.NoopListCallback
import com.rafarocar.albumlist.feature_albumList.domain.model.Album
import com.rafarocar.albumlist.feature_albumList.domain.repository.AlbumRepository
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetAlbumsUseCaseTest {

    private val mockedRepository = mockk<AlbumRepository>()
    lateinit var getAlbumsUseCase: GetAlbumsUseCase
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        getAlbumsUseCase = GetAlbumsUseCase(mockedRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `invoke should return albums from repository`() = runTest {
        val fakeAlbums = listOf(
            Album(1, 1, "Album 1", "url1", "thumb1"),
            Album(2, 1, "Album 2", "url2", "thumb2")
        )
        val fakePagingData: Flow<PagingData<Album>> = flowOf(PagingData.from(fakeAlbums))

        coEvery { mockedRepository.getAlbums() } returns fakePagingData

        // Collect PagingData using AsyncPagingDataDiffer
        val differ = AsyncPagingDataDiffer(
            diffCallback = AlbumDiffUtilForTests, // you need to provide this
            updateCallback = NoopListCallback(),
            workerDispatcher = dispatcher
        )

        val result = getAlbumsUseCase()
        result.let { pagingDataFlow ->
            pagingDataFlow.collect { pagingData ->
                differ.submitData(pagingData)
            }
        }

        advanceUntilIdle()

        // Collect the items from the PagingDataDiffer
        val items = mutableListOf<Album>()
        differ.snapshot().items.forEach { items.add(it) }

        // Assert that the items match the expected fake albums
        assertEquals(fakeAlbums.size, items.size)
        assertEquals(fakeAlbums[0].title, items[0].title)
    }
}