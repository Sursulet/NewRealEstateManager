package com.sursulet.realestatemanager.data.geocoder.responses

data class Geometry(
    val location: Location,
    val locationType: String,
    val viewport: Viewport
)
