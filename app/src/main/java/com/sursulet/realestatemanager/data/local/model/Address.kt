package com.sursulet.realestatemanager.data.local.model

import androidx.room.ColumnInfo

data class Address(
    val street: String,
    val extras: String = "",
    @ColumnInfo(name = "post_code") val postCode: String,
    val city: String,
    val state: String = "",
    val country: String
)
