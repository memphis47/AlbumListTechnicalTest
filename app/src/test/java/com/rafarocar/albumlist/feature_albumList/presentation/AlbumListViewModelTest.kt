package com.rafarocar.albumlist.feature_albumList.presentation

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import app.cash.turbine.test
import com.rafarocar.albumlist.feature_albumList.common.AlbumDiffUtilForTests
import com.rafarocar.albumlist.feature_albumList.common.NoopListCallback
import com.rafarocar.albumlist.feature_albumList.domain.model.Album
import com.rafarocar.albumlist.feature_albumList.domain.useCase.GetAlbumsUseCase
import com.rafarocar.albumlist.feature_albumList.domain.useCase.RefreshAlbumsUseCase
import com.rafarocar.albumlist.feature_albumList.presentation.intents.AlbumListEffect
import com.rafarocar.albumlist.feature_albumList.presentation.intents.AlbumListIntent
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
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
class AlbumListViewModelTest {

    private val getAlbumsUseCaseMock = mockk<GetAlbumsUseCase>()
    private val refreshAlbumsUseCaseMock = mockk<RefreshAlbumsUseCase>()
    private lateinit var viewModel: AlbumListViewModel

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)

        mockkStatic(android.util.Log::class)
        every { android.util.Log.i(any(), any()) } returns 0
        every { android.util.Log.d(any(), any()) } returns 0
        every { android.util.Log.e(any(), any(), any()) } returns 0
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when create viewModel init fetch albums and update state`() {
        runTest(dispatcher) {
            val fakeAlbums = listOf(
                Album(
                    id = 1,
                    albumId = 1,
                    title = "Album 1",
                    url = "url1",
                    thumbnailURL = "thumb1"
                ),
                Album(id = 2, albumId = 1, title = "Album 2", url = "url2", thumbnailURL = "thumb2")
            )

            // Flow<PagingData<Album>>
            val fakePagingFlow = flowOf(PagingData.from(fakeAlbums))

            coEvery { getAlbumsUseCaseMock() } returns fakePagingFlow

            viewModel = AlbumListViewModel(
                getAlbumsUseCase = getAlbumsUseCaseMock,
                refreshAlbumsUseCase = refreshAlbumsUseCaseMock
            )

            // Collect PagingData using AsyncPagingDataDiffer
            val differ = AsyncPagingDataDiffer(
                diffCallback = AlbumDiffUtilForTests, // you need to provide this
                updateCallback = NoopListCallback(),
                workerDispatcher = dispatcher
            )

            val result = viewModel.retrieveAlbums()
            result?.let { pagingDataFlow ->
                pagingDataFlow.collect { pagingData ->
                    differ.submitData(pagingData)
                }
            }

            advanceUntilIdle()

            val albumsFlow = viewModel.state.value.albums
            differ.submitData(albumsFlow.first())

            assertEquals(fakeAlbums, differ.snapshot().items)
        }
    }

    @Test
    fun `when call retrieveAlbums then viewModel return the flow of pagingData Album`() {
        runTest(dispatcher) {
            val fakeAlbums = listOf(
                Album(
                    id = 1,
                    albumId = 1,
                    title = "Album 1",
                    url = "url1",
                    thumbnailURL = "thumb1"
                ),
                Album(id = 2, albumId = 1, title = "Album 2", url = "url2", thumbnailURL = "thumb2")
            )

            // Flow<PagingData<Album>>
            val fakePagingFlow = flowOf(PagingData.from(fakeAlbums))

            coEvery { getAlbumsUseCaseMock() } returns fakePagingFlow

            viewModel = AlbumListViewModel(
                getAlbumsUseCase = getAlbumsUseCaseMock,
                refreshAlbumsUseCase = refreshAlbumsUseCaseMock
            )

            // Collect PagingData using AsyncPagingDataDiffer
            val differ = AsyncPagingDataDiffer(
                diffCallback = AlbumDiffUtilForTests, // you need to provide this
                updateCallback = NoopListCallback(),
                workerDispatcher = dispatcher
            )

            viewModel.state.value.albums.let { pagingDataFlow ->
                pagingDataFlow.collect { pagingData ->
                    differ.submitData(pagingData)
                }
            }
            advanceUntilIdle()

            val albumsFlow = viewModel.state.value.albums
            differ.submitData(albumsFlow.first())

            assertEquals(fakeAlbums, differ.snapshot().items)
        }
    }

    @Test
    fun `when call handleIntent with LoadAlbums intent then viewModel fetch albums and update state`() {
        runTest(dispatcher) {
            val fakeAlbums = listOf(
                Album(
                    id = 1,
                    albumId = 1,
                    title = "Album 1",
                    url = "url1",
                    thumbnailURL = "thumb1"
                ),
                Album(id = 2, albumId = 1, title = "Album 2", url = "url2", thumbnailURL = "thumb2")
            )

            // Flow<PagingData<Album>>
            val fakePagingFlow = flowOf(PagingData.from(fakeAlbums))

            coEvery { getAlbumsUseCaseMock() } returns fakePagingFlow

            viewModel = AlbumListViewModel(
                getAlbumsUseCase = getAlbumsUseCaseMock,
                refreshAlbumsUseCase = refreshAlbumsUseCaseMock
            )

            val newFakeAlbums = listOf(
                Album(
                    id = 3,
                    albumId = 3,
                    title = "Album 3",
                    url = "url3",
                    thumbnailURL = "thumb3"
                ),
                Album(
                    id = 4,
                    albumId = 4,
                    title = "Album 4",
                    url = "url4",
                    thumbnailURL = "thumb4"
                )
            )

            val newFakePagingFlow = flowOf(PagingData.from(newFakeAlbums))

            val newDiffer = AsyncPagingDataDiffer(
                diffCallback = AlbumDiffUtilForTests, // you need to provide this
                updateCallback = NoopListCallback(),
                workerDispatcher = dispatcher
            )

            coEvery { getAlbumsUseCaseMock() } returns newFakePagingFlow

            viewModel.handleIntent(AlbumListIntent.LoadAlbums)

            advanceUntilIdle()

            val newAlbumsFlow = viewModel.state.value.albums
            newDiffer.submitData(newAlbumsFlow.first())

            assertEquals(newFakeAlbums, newDiffer.snapshot().items)
        }
    }

    @Test
    fun `when call handleIntent with RefreshAlbums intent then useCase is called`() {
        runTest(dispatcher) {
            val fakeAlbums = listOf(
                Album(
                    id = 1,
                    albumId = 1,
                    title = "Album 1",
                    url = "url1",
                    thumbnailURL = "thumb1"
                ),
                Album(id = 2, albumId = 1, title = "Album 2", url = "url2", thumbnailURL = "thumb2")
            )

            // Flow<PagingData<Album>>
            val fakePagingFlow = flowOf(PagingData.from(fakeAlbums))

            coEvery { getAlbumsUseCaseMock() } returns fakePagingFlow

            viewModel = AlbumListViewModel(
                getAlbumsUseCase = getAlbumsUseCaseMock,
                refreshAlbumsUseCase = refreshAlbumsUseCaseMock
            )

            viewModel.handleIntent(AlbumListIntent.RefreshAlbums)

            advanceUntilIdle()

            coVerify(exactly = 1) { refreshAlbumsUseCaseMock() }
        }
    }

    @Test
    fun `when refreshAlbumsUseCase throws then ShowError is emitted`() = runTest {
        // Arrange
        val getAlbumsUseCaseMock = mockk<GetAlbumsUseCase>()
        val refreshAlbumsUseCaseMock = mockk<RefreshAlbumsUseCase>()

        coEvery { getAlbumsUseCaseMock() } returns flowOf(PagingData.empty())
        coEvery { refreshAlbumsUseCaseMock() } throws RuntimeException("refresh failed")

        val viewModel = AlbumListViewModel(
            getAlbumsUseCaseMock,
            refreshAlbumsUseCaseMock
        )

        // Act
        viewModel.handleIntent(AlbumListIntent.RefreshAlbums)
        advanceUntilIdle()

        // Assert
        viewModel.effect.test {
            val effect = awaitItem()
            assert(effect is AlbumListEffect.ShowError)
            assert((effect as AlbumListEffect.ShowError).message == "refresh failed")
            cancelAndConsumeRemainingEvents()
        }
    }
}