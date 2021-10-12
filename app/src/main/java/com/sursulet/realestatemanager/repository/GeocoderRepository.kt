package com.sursulet.realestatemanager.repository

import com.sursulet.realestatemanager.data.geocoder.GeocoderApi
import com.sursulet.realestatemanager.data.geocoder.responses.GeocoderResponse
import com.sursulet.realestatemanager.utils.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeocoderRepository @Inject constructor(
    private val geocoderApi: GeocoderApi
) {

    suspend fun getCoordinates(address: String): Resource<GeocoderResponse> {
        return try {
            val response = geocoderApi.getCoordinates(address)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("An unknown error occurred", null)
            } else {
                Resource.error("An unknown error occurred", null)
            }
        } catch (e: Exception) {
            Resource.error("Couldn't reach the server. Check your internet connection.", null)
        }
    }
}