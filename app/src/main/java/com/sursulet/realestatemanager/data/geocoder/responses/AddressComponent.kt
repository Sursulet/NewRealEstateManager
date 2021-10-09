package com.sursulet.realestatemanager.data.geocoder.responses

data class AddressComponent(
    var longName: String? = null,
    var shortName: String? = null,
    var types: List<String?>? = null
)
