package com.sursulet.realestatemanager.data.local.model

import androidx.room.Embedded
import androidx.room.Relation

data class RealEstateWithPhotos(
    @Embedded val realEstate: RealEstate,
    @Relation(parentColumn = "id", entityColumn = "realEstateId") val photos: List<Photo>
)
