package com.rafarocar.albumlist.feature_albumList.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.rafarocar.albumlist.feature_albumList.data.local.AlbumDao
import com.rafarocar.albumlist.feature_albumList.data.local.AlbumDatabase
import com.rafarocar.albumlist.feature_albumList.data.local.AlbumEntity
import com.rafarocar.albumlist.feature_albumList.data.mapper.toEntity
import com.rafarocar.albumlist.feature_albumList.data.remote.AlbumApi
import com.rafarocar.albumlist.feature_albumList.data.remote.AlbumDTO
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalPagingApi::class)
class AlbumRemoteMediatorTest {

    private val albumApi = mockk<AlbumApi>()
    private val albumDao = mockk<AlbumDao>(relaxed = true)
    private val albumDatabase = mockk<AlbumDatabase>()

    private lateinit var mediator: AlbumRemoteMediator

    @Before
    fun setup() {
        every { albumDatabase.albumDao() } returns albumDao

        mockkStatic(android.util.Log::class)
        every { android.util.Log.i(any(), any()) } returns 0
        every { android.util.Log.d(any(), any()) } returns 0
        every { android.util.Log.e(any(), any()) } returns 0
        every { android.util.Log.e(any(), any(), any()) } returns 0

        // RoomDatabase requires this when using withTransaction
        // https://stackoverflow.com/questions/56500576/how-to-mock-android-room-withtransaction-method-with-mockk
        mockkStatic(
            "androidx.room.RoomDatabaseKt"
        )

        val transactionLambda = slot<suspend () -> Unit>()
        coEvery { albumDatabase.withTransaction(capture(transactionLambda)) } coAnswers {
            transactionLambda.captured.invoke()
        }

        mediator = AlbumRemoteMediator(albumApi, albumDatabase)
    }

    @Test
    fun `load REFRESH inserts albums into database`() = runTest {
        val fakeAlbums = listOf(
            AlbumDTO(id = 1, albumId = 1, title = "Test", url = "url", thumbnailURL = "thumb")
        )

        coEvery { albumApi.getAlbums() } returns fakeAlbums
        coEvery { albumDao.clearAll() } just Runs
        coEvery { albumDao.insertAlbums(any()) } just Runs

        val result = mediator.load(
            LoadType.REFRESH,
            PagingState(pages = listOf(), anchorPosition = null, config = PagingConfig(20), leadingPlaceholderCount = 0)
        )

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        coVerify { albumDao.clearAll() }
        coVerify { albumDao.insertAlbums(fakeAlbums.map { it.toEntity() }) }
    }

    @Test
    fun `load with APPEND does not call clear or insert`() = runTest {
        val fakeAlbums = listOf(
            AlbumDTO(id = 1, albumId = 1, title = "Test", url = "url", thumbnailURL = "thumb")
        )

        coEvery { albumApi.getAlbums() } returns fakeAlbums
        coEvery { albumDao.clearAll() } just Runs
        coEvery { albumDao.insertAlbums(any()) } just Runs

        val result = mediator.load(
            LoadType.APPEND,
            PagingState(pages = listOf(), anchorPosition = null, config = PagingConfig(20), leadingPlaceholderCount = 0)
        )


        assertTrue(result is RemoteMediator.MediatorResult.Success)
        coVerify(exactly = 0) { albumDao.clearAll() }
        coVerify(exactly = 0) { albumDao.insertAlbums(any()) }
    }

    @Test
    fun `load throws exception returns Error`() = runTest {
        // Arrange
        coEvery { albumApi.getAlbums() } throws RuntimeException("network error")

        val state = PagingState<Int, AlbumEntity>(
            pages = emptyList(),
            anchorPosition = null,
            config = PagingConfig(pageSize = 20),
            leadingPlaceholderCount = 0
        )

        // Act
        val result = mediator.load(LoadType.REFRESH, state)

        // Assert
        assertTrue(result is RemoteMediator.MediatorResult.Error)
    }
}