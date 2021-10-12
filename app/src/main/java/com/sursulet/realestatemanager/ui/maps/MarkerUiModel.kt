package com.sursulet.realestatemanager.ui.maps

import com.google.android.gms.maps.model.LatLng

data class MarkerUiModel(
    val id: Long,
    val coordinates: LatLng,
    val distance: Float = 0f
)
