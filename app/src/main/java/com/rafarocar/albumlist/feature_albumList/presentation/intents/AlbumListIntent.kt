package com.rafarocar.albumlist.feature_albumList.presentation.intents

/**
 * Intent to handle the states of the AlbumListScreen
 */
sealed class AlbumListIntent {
    object LoadAlbums : AlbumListIntent()
    object RefreshAlbums : AlbumListIntent()
}