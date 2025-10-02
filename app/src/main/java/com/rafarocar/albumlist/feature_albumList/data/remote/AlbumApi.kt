package com.rafarocar.albumlist.feature_albumList.data.remote

import retrofit2.http.GET

/**
 * Interface to define the methods to use in the API
 */
interface AlbumApi {

    @GET("img/shared/technical-test.json")
    suspend fun getAlbums(): List<AlbumDTO>

}