package com.sursulet.realestatemanager.data.geocoder

import com.sursulet.realestatemanager.BuildConfig
import com.sursulet.realestatemanager.data.geocoder.responses.GeocoderResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocoderApi {

    @GET("maps/api/geocode/json?")
    suspend fun getCoordinates(
        @Query("address") address: String,
        @Query("key") key: String = BuildConfig.GOOGLE_MAPS_KEY
    ): Response<GeocoderResponse>
}