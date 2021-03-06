package com.sursulet.realestatemanager.ui.maps

import android.location.Location

data class MapsState(
    val isMapReady: Boolean = false,
    val lastLocation: Location = Location(""),
    val zoomLvl: Float = 12f,
    val markers: List<MarkerUiModel> = emptyList()
)
