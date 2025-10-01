package com.rafarocar.albumlist.feature_albumList.data.api

import com.rafarocar.albumlist.feature_albumList.data.model.AlbumDTO
import retrofit2.http.GET

interface AlbumApi {

    @GET("img/shared/technical-test.json")
    suspend fun getAlbums(): List<AlbumDTO>

}