package com.sursulet.realestatemanager.data.geocoder.responses

data class GeocoderResponse(
    val results: List<Result>,
    val status: String
)
