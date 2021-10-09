package com.sursulet.realestatemanager.ui.gallery

import com.sursulet.realestatemanager.ui.adapters.PhotoUiModel

data class GalleryState(
    val id: Long? = null,
    val isTwoPane: Boolean = false,
    val photos: List<PhotoUiModel> = emptyList()
)
