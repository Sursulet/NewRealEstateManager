package com.sursulet.realestatemanager.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sursulet.realestatemanager.data.local.dao.PhotoDao
import com.sursulet.realestatemanager.data.local.dao.RealEstateDao
import com.sursulet.realestatemanager.data.local.model.Photo
import com.sursulet.realestatemanager.data.local.model.RealEstate

@TypeConverters(Converters::class)
@Database(entities = [RealEstate::class, Photo::class], version = 1, exportSchema = false)
abstract class RealEstateDatabase : RoomDatabase() {

    abstract fun realEstateDao(): RealEstateDao
    abstract fun photoDao(): PhotoDao
}