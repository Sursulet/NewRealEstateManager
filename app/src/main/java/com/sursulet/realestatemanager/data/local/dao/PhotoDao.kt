package com.sursulet.realestatemanager.data.local.dao

import androidx.room.*
import com.sursulet.realestatemanager.data.local.model.Photo
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {

    @Query("SELECT * FROM photo WHERE realEstateId=:realEstateId")
    fun getPhotos(realEstateId: Long): Flow<List<Photo>>

    @Query("SELECT * FROM photo WHERE id=:id")
    fun getPhoto(id: Long): Flow<Photo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(photos: List<Photo>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(photo: Photo)

    @Update
    suspend fun update(photo: Photo)

    @Delete
    suspend fun delete(photo: Photo)
}