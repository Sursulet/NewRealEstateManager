package com.sursulet.realestatemanager.ui.photo

import android.graphics.Bitmap

data class PhotoState(
    val id: Long? = null,
    val title: String = "",
    val image: Bitmap? = null,
    val errorTitle: String? = null,
    val realEstateId: Long = -1
)
