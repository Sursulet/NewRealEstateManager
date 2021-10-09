package com.sursulet.realestatemanager.ui.adapters

import android.graphics.Bitmap
import android.graphics.Color
import com.sursulet.realestatemanager.R

data class RealEstateUiModel(
    val id: Long,
    val image: Bitmap,
    val type: String,
    val city:String,
    val price: String,
    val backgroundColorRes: Int = Color.WHITE,
    val textColor: Int = R.color.purple_500
)
