package com.sursulet.realestatemanager.ui.photo

import android.graphics.Bitmap

sealed class PhotoEvent {
    data class Title(val value: String) : PhotoEvent()
    data class Image(val value: Bitmap) : PhotoEvent()
    object OnSave : PhotoEvent()
}