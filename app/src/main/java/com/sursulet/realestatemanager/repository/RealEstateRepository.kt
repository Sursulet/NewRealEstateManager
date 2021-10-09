package com.sursulet.realestatemanager.repository

import com.sursulet.realestatemanager.data.local.dao.RealEstateDao
import com.sursulet.realestatemanager.data.local.model.RealEstate
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RealEstateRepository @Inject constructor(
    private val dao: RealEstateDao
) {

    fun getRealEstatesWithPhotos() = dao.getRealEstatesWithPhotos()
    fun getRealEstateWithPhotos(id: Long) = dao.getRealEstateWithPhotos(id)
    fun getRealEstates() = dao.getRealEstates()
    fun getRealEstate(id: Long) = dao.getRealEstate(id)
    suspend fun insert(realEstate: RealEstate) = dao.insert(realEstate)
    suspend fun update(realEstate: RealEstate) = dao.update(realEstate)
    fun search(
        type: String,
        zone: String,
        minPrice: Float,
        maxPrice: Float,
        release: LocalDate?,
        isAvailable: Boolean,
        minSurface: Int,
        maxSurface: Int,
        nearest: String,
        size: Int
    ) = dao.search(
        type = type,
        zone = zone,
        minPrice = minPrice,
        maxPrice = maxPrice,
        release = release,
        isAvailable = isAvailable,
        minSurface = minSurface,
        maxSurface = maxSurface,
        nearest = nearest,
        size = size
    )
}