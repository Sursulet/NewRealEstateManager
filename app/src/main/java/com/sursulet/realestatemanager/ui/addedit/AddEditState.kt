package com.sursulet.realestatemanager.ui.addedit

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
    val isAvailable: Boolean = false,
    val created: String = "",
    val sold: String = "",
    val agent: String = "",
    val error: AddEditError = AddEditError()
)
