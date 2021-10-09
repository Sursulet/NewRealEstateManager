package com.sursulet.realestatemanager.repository.shared

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RealEstateIdRepository @Inject constructor() {

    private val _realEstateId = MutableStateFlow<Long?>(null)
    val realEstateId = _realEstateId.asStateFlow()

    fun setValue(newId: Long) {
        _realEstateId.value = newId
    }
}