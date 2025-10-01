package com.rafarocar.albumlist.feature_albumList.domain.useCase

import androidx.paging.PagingData
import com.rafarocar.albumlist.feature_albumList.domain.model.Album
import com.rafarocar.albumlist.feature_albumList.domain.repository.AlbumRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAlbumsUseCase @Inject constructor(
    private val repository: AlbumRepository
) {

    operator fun invoke(): Flow<PagingData<Album>> {
        return repository.getAlbums().flow
    }
}