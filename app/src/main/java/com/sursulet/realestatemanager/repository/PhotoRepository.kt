package com.sursulet.realestatemanager.repository

import com.sursulet.realestatemanager.data.local.dao.PhotoDao
import com.sursulet.realestatemanager.data.local.model.Photo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotoRepository @Inject constructor(private val dao: PhotoDao) {

    fun getPhotos(realEstateId: Long) = dao.getPhotos(realEstateId)
    suspend fun insert(photo: Photo) = dao.insert(photo)
    suspend fun update(photo: Photo) = dao.update(photo)
}