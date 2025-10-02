package com.rafarocar.albumlist.feature_albumList.presentation.intents

import androidx.paging.PagingData
import com.rafarocar.albumlist.feature_albumList.domain.model.Album
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

/**
 * State of the AlbumListScreen, following the MVI Architecture
 */
data class AlbumListState(
    val albums: Flow<PagingData<Album>> = emptyFlow(),
    val isLoading: Boolean = false,
    val error: String? = null
)