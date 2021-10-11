package com.sursulet.realestatemanager.ui.addedit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sursulet.realestatemanager.data.local.model.Address
import com.sursulet.realestatemanager.data.local.model.RealEstate
import com.sursulet.realestatemanager.di.IoDispatcher
import com.sursulet.realestatemanager.repository.RealEstateRepository
import com.sursulet.realestatemanager.repository.shared.RealEstateIdRepository
import com.sursulet.realestatemanager.repository.shared.TwoPaneRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import java.time.LocalDate
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class AddEditViewModel @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val twoPaneRepository: TwoPaneRepository,
    private val realEstateIdRepository: RealEstateIdRepository,
    private val realEstateRepository: RealEstateRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditState())
    val uiState = _uiState.asStateFlow()

    private val _navigation = Channel<AddEditNavigation>(capacity = Channel.CONFLATED)
    val navigation = _navigation.receiveAsFlow()
        .shareIn(viewModelScope.plus(dispatcher), SharingStarted.WhileSubscribed())

    init {
        viewModelScope.launch(dispatcher) {
            realEstateIdRepository.realEstateId.filterNotNull().flatMapLatest { id ->
                realEstateRepository.getRealEstate(id)
            }.combine(twoPaneRepository.twoPane) { estate: RealEstate, twoPane: Boolean ->
                AddEditState(isTwoPane = twoPane, estate = estate)
            }.collect { state ->
                _uiState.update {
                    it.copy(
                        estate = state.estate,
                        title = "Edit Real Estate",
                        type = state.estate?.type ?: "",
                        price = state.estate?.price.toString(),
                        surface = state.estate?.surface.toString(),
                        rooms = state.estate?.rooms.toString(),
                        bathrooms = state.estate?.bathrooms?.toString() ?: "",
                        bedrooms = state.estate?.bedrooms?.toString() ?: "",
                        description = state.estate?.description.toString(),
                        street = state.estate?.address?.street.toString(),
                        extras = state.estate?.address?.extras.toString(),
                        city = state.estate?.address?.city.toString(),
                        state = state.estate?.address?.state.toString(),
                        postCode = state.estate?.address?.postCode.toString(),
                        country = state.estate?.address?.country.toString(),
                        nearest = state.estate?.nearest.toString(),
                        isAvailable = (state.estate?.sold == null),
                        created = state.estate?.created.toString(),
                        sold = state.estate?.sold.toString(),
                        agent = state.estate?.agent.toString(),
                    )
                }
            }
        }
    }

    fun onEvent(event: AddEditEvent) {
        when (event) {
            is AddEditEvent.IsAvailable -> { _uiState.update { it.copy(isAvailable = event.value) } }
            is AddEditEvent.Type -> { _uiState.update { it.copy(type = event.value) } }
            is AddEditEvent.Price -> { _uiState.update { it.copy(price = event.value) } }
            is AddEditEvent.Surface -> { _uiState.update { it.copy(surface = event.value) } }
            is AddEditEvent.Rooms -> { _uiState.update { it.copy(rooms = event.value) } }
            is AddEditEvent.Bedrooms -> { _uiState.update { it.copy(bedrooms = event.value) } }
            is AddEditEvent.Bathrooms -> { _uiState.update { it.copy(bathrooms = event.value) } }
            is AddEditEvent.Description -> { _uiState.update { it.copy(description = event.value) } }
            is AddEditEvent.Street -> { _uiState.update { it.copy(street = event.value) } }
            is AddEditEvent.Extras -> { _uiState.update { it.copy(extras = event.value) } }
            is AddEditEvent.PostCode -> { _uiState.update { it.copy(postCode = event.value) } }
            is AddEditEvent.City -> { _uiState.update { it.copy(city = event.value) } }
            is AddEditEvent.State -> { _uiState.update { it.copy(state = event.value) } }
            is AddEditEvent.Country -> { _uiState.update { it.copy(country = event.value) } }
            is AddEditEvent.Nearest -> { _uiState.update { it.copy(nearest = event.value) } }
            is AddEditEvent.Agent -> { _uiState.update { it.copy(agent = event.value) } }
            is AddEditEvent.NotSave -> {
                _uiState.update { it.copy(isSave = event.value) }
                if (event.value) {
                    if (uiState.value.isTwoPane) { _navigation.trySend(AddEditNavigation.DetailActivity) }
                    else { _navigation.trySend(AddEditNavigation.DetailFragment) }
                }
            }
            AddEditEvent.OnSave -> {
                _uiState.update {
                    it.copy(
                        error = it.error.copy(
                            type = if (it.type.isBlank()) "You must enter a type" else null,
                            price = if (it.price.isBlank()) "You must enter a price" else null,
                            surface = if (uiState.value.price.isBlank()) "You must enter a surface" else null,
                            rooms = if (uiState.value.rooms.isBlank()) "You must enter a rooms" else null,
                            description = if (uiState.value.description.isBlank()) "You must enter a description" else null,
                            street = if (uiState.value.street.isBlank()) "You must enter a street" else null,
                            city = if (uiState.value.city.isBlank()) "You must enter a city" else null,
                            postCode = if (uiState.value.postCode.isBlank()) "You must enter a post code" else null,
                            country = if (uiState.value.country.isBlank()) "You must enter a country" else null,
                            agent = if (uiState.value.agent.isBlank()) "You must enter a agent" else null
                        )
                    )
                }

                if (uiState.value.error != AddEditError(null, null, null, null, null, null, null, null, null, null)) return

                save()
            }
        }
    }

    private fun save() {
        val estate = uiState.value.estate
        if (estate != null) {
            val updateRealEstate = estate.copy(
                type = uiState.value.type,
                price = uiState.value.price.toFloat(),
                surface = uiState.value.surface.toDouble(),
                rooms = uiState.value.rooms.toInt(),
                bedrooms = uiState.value.bedrooms.toIntOrNull(),
                bathrooms = uiState.value.bathrooms.toIntOrNull(),
                description = uiState.value.description,
                address = Address(
                    street = uiState.value.street,
                    extras = uiState.value.extras,
                    city = uiState.value.city,
                    state = uiState.value.state,
                    postCode = uiState.value.postCode,
                    country = uiState.value.country
                ),
                created = LocalDate.parse(uiState.value.created),
                sold = if (uiState.value.isAvailable) null else LocalDate.now(),
                agent = uiState.value.agent

            )

            updateRealEstate(updateRealEstate)
        } else {
            val newRealEstate = RealEstate(
                type = uiState.value.type,
                price = uiState.value.price.toFloat(),
                surface = uiState.value.surface.toDouble(),
                rooms = uiState.value.rooms.toInt(),
                bedrooms = uiState.value.bedrooms.toIntOrNull(),
                bathrooms = uiState.value.bathrooms.toIntOrNull(),
                description = uiState.value.description,
                address = Address(
                    street = uiState.value.street,
                    extras = uiState.value.extras,
                    city = uiState.value.city,
                    state = uiState.value.state,
                    postCode = uiState.value.postCode,
                    country = uiState.value.country
                ),
                sold = if (uiState.value.isAvailable) null else LocalDate.now(),
                nearest = uiState.value.extras,
                agent = uiState.value.agent
            )
            createRealEstate(newRealEstate)
        }

        _navigation.trySend(AddEditNavigation.GalleryDialogFragment)
    }

    private fun createRealEstate(realEstate: RealEstate) = viewModelScope.launch(dispatcher) {
        val id = realEstateRepository.insert(realEstate)
        realEstateIdRepository.setValue(id)
    }

    private fun updateRealEstate(realEstate: RealEstate) = viewModelScope.launch(dispatcher) {
        realEstateRepository.update(realEstate)
    }

}