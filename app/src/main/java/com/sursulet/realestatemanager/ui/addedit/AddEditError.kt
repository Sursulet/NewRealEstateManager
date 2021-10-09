package com.sursulet.realestatemanager.ui.addedit

data class AddEditError(
    val type: String? = null,
    val price: String? = null,
    val surface: String? = null,
    val rooms: String? = null,
    val bathrooms: String? = null,
    val bedrooms: String? = null,
    val description: String? = null,
    val street : String? = null,
    val extras: String? = null,
    val city : String? = null,
    val state : String? = null,
    val postCode : String? = null,
    val country : String? = null,
    val nearest: String? = null,
    val agent: String? = null
)
