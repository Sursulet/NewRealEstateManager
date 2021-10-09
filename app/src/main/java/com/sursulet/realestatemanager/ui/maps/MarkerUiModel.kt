package com.sursulet.realestatemanager.ui.maps

import android.location.Location

data class MarkerUiModel(
    val id: Long,
    val location: Location,
    val distance: Float
)
