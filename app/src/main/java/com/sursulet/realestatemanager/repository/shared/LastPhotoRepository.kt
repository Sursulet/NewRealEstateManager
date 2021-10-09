package com.sursulet.realestatemanager.repository.shared

import com.sursulet.realestatemanager.ui.adapters.PhotoUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LastPhotoRepository @Inject constructor() {

    private val _photo = MutableStateFlow<PhotoUiModel?>(null)
    val photo = _photo.asStateFlow()

    fun setValue(newPhoto: PhotoUiModel) {
        _photo.value = newPhoto
    }
}