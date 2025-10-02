package com.rafarocar.albumlist.feature_albumList.domain.useCase

import com.rafarocar.albumlist.feature_albumList.domain.repository.AlbumRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RefreshAlbumsUseCaseTest {

    private val mockedRepository = mockk<AlbumRepository>()
    lateinit var refreshAlbumsUseCase: RefreshAlbumsUseCase
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        refreshAlbumsUseCase = RefreshAlbumsUseCase(mockedRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `invoke should call refreshAlbums in repository`() = runTest {
        coEvery { mockedRepository.refreshAlbums() } returns Unit

        refreshAlbumsUseCase()

        advanceUntilIdle()

        coVerify(exactly = 1) { mockedRepository.refreshAlbums() }
    }
}