package com.rafarocar.albumlist.feature_albumList.data.remote

import retrofit2.http.GET

interface AlbumApi {

    @GET("img/shared/technical-test.json")
    suspend fun getAlbums(): List<AlbumDTO>

}