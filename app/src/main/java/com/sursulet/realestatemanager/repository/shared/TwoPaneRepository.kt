package com.sursulet.realestatemanager.repository.shared

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TwoPaneRepository @Inject constructor() {

    private val _twoPane = MutableStateFlow(false)
    val twoPane = _twoPane.asStateFlow()

    fun setValue(twoPane: Boolean) {
        _twoPane.value = twoPane
    }
}