package com.sursulet.realestatemanager.ui.maps

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.sursulet.realestatemanager.di.IoDispatcher
import com.sursulet.realestatemanager.repository.GeocoderRepository
import com.sursulet.realestatemanager.repository.RealEstateRepository
import com.sursulet.realestatemanager.utils.awaitLastLocation
import com.sursulet.realestatemanager.utils.locationFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class MapsViewModel @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val client: FusedLocationProviderClient,
    private val geocoderRepository: GeocoderRepository,
    private val realEstateRepository: RealEstateRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapsState())
    val uiState = _uiState.asStateFlow()

    private val _errorMessage = Channel<String>(capacity = Channel.CONFLATED)
    val navigation = _errorMessage.receiveAsFlow()
        .shareIn(viewModelScope.plus(dispatcher), SharingStarted.WhileSubscribed())

    init {
        viewModelScope.launch {
            val response = geocoderRepository.getCoordinates("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA")
        }
    }

    /*
    init {
        viewModelScope.launch(dispatcher) {
            combine(realEstateFlow, lastLocation, radius) { estates, location, radius ->
                estates.map { estate ->
                    val coordinates: Location? = geocoderRepository.getCoordinates("address").let {
                        if (it.status == Status.SUCCESS) it.data?.results?.first()?.geometry?.location?.let {
                            Location("").apply {
                                latitude = it.lat
                                longitude = it.lng
                            }
                        }
                        else null
                    }

                    val distance = location.distanceTo(coordinates)
                    MarkerUiModel(estate.id, distance)
                }.filter {
                    val distance = location.distanceTo(location)
                    distance < radius
                }

            }.collect { list ->
                _uiState.update { it.copy(markers = list) }
            }
        }
    }

     */

    fun onEvent(event: MapsEvent) {
        when (event) {
            MapsEvent.MapReady -> { _uiState.update { it.copy(isMapReady = true) } }
            MapsEvent.LastKnownLocation -> { getLastKnownLocation() }
            MapsEvent.StartUpdatingLocation -> { startUpdatingLocation() }
            is MapsEvent.Zoom -> { _uiState.update { it.copy(zoomLvl = event.value) } }
        }
    }

    private fun getLastKnownLocation() = viewModelScope.launch {
        try {
            val lastLocation = client.awaitLastLocation()
            _uiState.update { it.copy(lastLocation = lastLocation) }
        } catch (e: Exception) {
            _errorMessage.trySend("Unable to get location")
        }
    }

    private fun startUpdatingLocation() = viewModelScope.launch(dispatcher) {
        client.locationFlow()
            .conflate()
            .catch { e ->
                _errorMessage.trySend("Unable to get location")
            }.collect { location ->
                _uiState.update { it.copy(lastLocation = location) }
            }
    }



}