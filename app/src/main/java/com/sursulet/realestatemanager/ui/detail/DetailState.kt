package com.sursulet.realestatemanager.ui.detail

import com.google.android.gms.maps.model.LatLng
import com.sursulet.realestatemanager.ui.adapters.PhotoUiModel

data class DetailState(
    val id: Long = -1,
    val media: List<PhotoUiModel> = emptyList(),
    val description: String = "",
    val surface: String = "",
    val rooms: String = "",
    val bathrooms: String = "",
    val bedrooms: String = "",
    val location: String = "",
    val coordinates: LatLng? = null
)
