package com.sursulet.realestatemanager.data.local.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class RealEstate(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String,
    val price: Float,
    val surface: Double,
    val rooms: Int,
    val bathrooms: Int,
    val bedrooms: Int,
    val description: String,
    @Embedded val address: Address,
    val nearest: String? = null,
    val isAvailable: Boolean = true,
    val created: LocalDate = LocalDate.now(),
    val sold: LocalDate? = null,
    val agent: String
)
