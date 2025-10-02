package com.rafarocar.albumlist.feature_albumList.presentation.intents

/**
 * Effect to handle the states of the AlbumListScreen
 */
sealed class AlbumListEffect {
    data class ShowError(val message: String) : AlbumListEffect()
}