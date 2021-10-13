package com.sursulet.realestatemanager.ui.gallery

import com.sursulet.realestatemanager.ui.adapters.PhotoUiModel

sealed class GalleryEvent {
    data class OnEdit(val photo: PhotoUiModel) : GalleryEvent()
    object OnCancel : GalleryEvent()
    object OnSave : GalleryEvent()
}
