package com.rafarocar.albumlist.feature_albumList.presentation.intents

sealed class AlbumListEffect {
    data class ShowError(val message: String) : AlbumListEffect()
}