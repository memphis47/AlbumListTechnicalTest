package com.rafarocar.albumlist.feature_albumList.presentation.intents

data class AlbumListState(
    val isLoading: Boolean = false,
    val error: String? = null
)