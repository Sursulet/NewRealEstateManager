package com.sursulet.realestatemanager.data.geocoder.responses

data class Result(
    val addressComponents: List<AddressComponent>,
    val formattedAddress: String,
    val geometry: Geometry,
    val placeId: String,
    val plusCode: PlusCode,
    val types: List<String>
)
