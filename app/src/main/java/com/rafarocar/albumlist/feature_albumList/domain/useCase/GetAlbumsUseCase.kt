package com.rafarocar.albumlist.feature_albumList.domain.useCase

import androidx.paging.PagingData
import com.rafarocar.albumlist.feature_albumList.domain.model.Album
import com.rafarocar.albumlist.feature_albumList.domain.repository.AlbumRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to get the albums from the repository
 * @param repository The repository to use
 */
class GetAlbumsUseCase @Inject constructor(
    private val repository: AlbumRepository
) {

    operator fun invoke(): Flow<PagingData<Album>> {
        return repository.getAlbums()
    }
}