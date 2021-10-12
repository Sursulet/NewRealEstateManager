package com.sursulet.realestatemanager.ui.gallery

sealed class GalleryNavigation {
    object Cancel : GalleryNavigation()
    object CloseActivity : GalleryNavigation()
    object CloseFragment : GalleryNavigation()
    data class EmptyGallery(val value: String = "you must enter at least one photo before leaving") : GalleryNavigation()
}
