package com.sursulet.realestatemanager.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sursulet.realestatemanager.data.geocoder.responses.Location
import com.sursulet.realestatemanager.di.IoDispatcher
import com.sursulet.realestatemanager.repository.GeocoderRepository
import com.sursulet.realestatemanager.repository.RealEstateRepository
import com.sursulet.realestatemanager.repository.shared.RealEstateIdRepository
import com.sursulet.realestatemanager.ui.adapters.PhotoUiModel
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
class DetailViewModel @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
    private val realEstateIdRepository: RealEstateIdRepository,
    private val realEstateRepository: RealEstateRepository,
    private val geocoderRepository: GeocoderRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailState())
    val uiState = _uiState.asStateFlow()

    private val _errorMessage = Channel<String>(capacity = Channel.CONFLATED)
    val navigation = _errorMessage.receiveAsFlow()
        .shareIn(viewModelScope.plus(dispatcher), SharingStarted.WhileSubscribed())

    init {
        viewModelScope.launch(dispatcher) {
            realEstateIdRepository.realEstateId.filterNotNull().flatMapLatest {
                realEstateRepository.getRealEstateWithPhotos(it)
            }.collect { estate ->
                val addressString = estate.realEstate.address.let { "${it.street}, ${it.postCode} ${it.city}, ${it.country}" }
                _uiState.update { state ->
                    state.copy(
                        media= estate.photos.map { PhotoUiModel(it.id,it.title,it.image) },
                        id = estate.realEstate.id,
                        description = estate.realEstate.description,
                        surface = estate.realEstate.surface.toString(),
                        rooms = estate.realEstate.rooms.toString(),
                        bathrooms = estate.realEstate.bathrooms.toString(),
                        bedrooms = estate.realEstate.bedrooms.toString(),
                        location = addressString,
                        coordinates = Location(48.866667,2.333333)
                    )
                }

            }
        }
    }

}