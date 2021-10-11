package com.sursulet.realestatemanager.ui.gallery

import com.sursulet.realestatemanager.ui.adapters.PhotoUiModel

sealed class GalleryEvent {
    data class OnEdit(val photo: PhotoUiModel) : GalleryEvent()
    //object Delete : GalleryEvent()
    object OnClose : GalleryEvent()
    object OnSave : GalleryEvent()
}
