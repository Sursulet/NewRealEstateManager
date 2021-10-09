package com.sursulet.realestatemanager.ui.maps

import android.location.Location

data class MapsState(
    val isMapReady: Boolean = false,
    //val lastLocation: LatLng = LatLng(0.0,0.0),
    val lastLocation: Location = Location(""),
    val zoomLvl: Float = 10f,
    val markers: List<MarkerUiModel> = emptyList()
)
