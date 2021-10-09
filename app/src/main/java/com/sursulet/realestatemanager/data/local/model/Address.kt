package com.sursulet.realestatemanager.data.local.model

import androidx.room.ColumnInfo

data class Address(
    val street: String,
    val extras: String? = null,
    val state: String,
    val city: String,
    @ColumnInfo(name = "post_code") val postCode: String,
    val country: String
)
