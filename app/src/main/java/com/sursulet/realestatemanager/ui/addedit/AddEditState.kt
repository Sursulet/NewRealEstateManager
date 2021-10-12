package com.sursulet.realestatemanager.ui.addedit

import android.view.View
import com.sursulet.realestatemanager.data.local.model.RealEstate

data class AddEditState(
    val isTwoPane: Boolean = false,
    val estate: RealEstate? = null,
    val title: String = "Add Real Estate",
    val type: String = "",
    val price: String = "",
    val surface: String = "",
    val rooms: String = "",
    val bathrooms: String = "",
    val bedrooms: String = "",
    val description: String = "",
    val street : String = "",
    val extras: String = "",
    val city : String = "",
    val state : String = "",
    val postCode : String = "",
    val country : String = "",
    val nearest: String = "",
    val isAvailable: Boolean = true,
    val created: String = "",
    val sold: String = "",
    val agent: String = "",
    val isSave: Boolean = false,
    val saveBtn: Int = View.GONE,
    val error: AddEditError = AddEditError()
)
