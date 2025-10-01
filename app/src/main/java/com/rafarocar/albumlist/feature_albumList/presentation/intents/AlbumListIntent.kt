package com.rafarocar.albumlist.feature_albumList.presentation.intents

sealed class AlbumListIntent {
    object LoadAlbums : AlbumListIntent()
}