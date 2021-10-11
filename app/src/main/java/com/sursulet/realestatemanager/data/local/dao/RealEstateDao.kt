package com.sursulet.realestatemanager.data.local.dao

import androidx.room.*
import com.sursulet.realestatemanager.data.local.model.RealEstate
import com.sursulet.realestatemanager.data.local.model.RealEstateWithPhotos
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface RealEstateDao {

    @Transaction
    @Query("SELECT * FROM realestate")
    fun getRealEstatesWithPhotos(): Flow<List<RealEstateWithPhotos>>

    @Transaction
    @Query("SELECT * FROM realestate WHERE id = :id")
    fun getRealEstateWithPhotos(id: Long): Flow<RealEstateWithPhotos>

    @Query("SELECT * FROM realestate")
    fun getRealEstates(): Flow<List<RealEstate>>

    @Query("SELECT * FROM realestate WHERE id = :id")
    fun getRealEstate(id: Long): Flow<RealEstate>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(realEstate: RealEstate): Long

    @Update
    suspend fun update(realEstate: RealEstate)

    @Transaction
    @Query("SELECT * FROM realestate WHERE type LIKE '%' || :type || '%' AND city LIKE '%' || :city || '%' AND price > :minPrice AND price < :maxPrice AND isAvailable = :isAvailable AND created >= :date AND surface > :minSurface AND surface < :maxSurface AND nearest LIKE '%' || :nearest || '%' AND EXISTS (SELECT COUNT(*) FROM photo WHERE photo.realEstateId = realestate.id GROUP BY realEstateId HAVING COUNT(*) >= :size)")
    fun search(
        type: String,
        city: String,
        minPrice: Float,
        maxPrice: Float,
        isAvailable: Boolean,
        date: LocalDate?,
        minSurface: Int,
        maxSurface: Int,
        nearest: String,
        size: Int
    ): Flow<List<RealEstateWithPhotos>>
}