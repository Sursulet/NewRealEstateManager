package com.sursulet.realestatemanager.ui.maps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.sursulet.realestatemanager.di.IoDispatcher
import com.sursulet.realestatemanager.repository.GeocoderRepository
import com.sursulet.realestatemanager.repository.RealEstateRepository
import com.sursulet.realestatemanager.utils.Others.formattedAddress
import com.sursulet.realestatemanager.utils.Status
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

            realEstateRepository.getRealEstates().map { estates ->
                estates.map { estate ->

                    val address: String = formattedAddress(estate.address)
                    val response = geocoderRepository.getCoordinates(address)

                    when (response.status) {
                        Status.SUCCESS -> {
                            val coordinates =
                                response.data!!.results.firstNotNullOf { it.geometry.location }
                                    .let { LatLng(it.lat, it.lng) }

                            MarkerUiModel(id = estate.id, coordinates = coordinates)
                        }
                        else -> { MarkerUiModel(id = estate.id, coordinates = LatLng(0.0, 0.0)) }
                    }
                }.filter { it.coordinates != LatLng(0.0, 0.0) }
            }.collect { list ->
                _uiState.update { it.copy(markers = list) }
            }
        }
    }

    fun onEvent(event: MapsEvent) {
        when (event) {
            MapsEvent.MapReady -> {
                _uiState.update { it.copy(isMapReady = true) }
            }
            MapsEvent.LastKnownLocation -> {
                getLastKnownLocation()
            }
            MapsEvent.StartUpdatingLocation -> {
                startUpdatingLocation()
            }
            is MapsEvent.Zoom -> {
                _uiState.update { it.copy(zoomLvl = event.value) }
            }
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
                _errorMessage.trySend("Unable to get location ${e.message}")
            }.collect { location ->
                _uiState.update { it.copy(lastLocation = location) }
            }
    }
}