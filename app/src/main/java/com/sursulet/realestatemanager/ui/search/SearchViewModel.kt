package com.sursulet.realestatemanager.ui.search

import androidx.lifecycle.ViewModel
import com.sursulet.realestatemanager.data.local.model.Address
import com.sursulet.realestatemanager.repository.shared.SearchQueryRepository
import com.sursulet.realestatemanager.utils.Others.calculatePeriod
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchQueryRepository: SearchQueryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.NbPhotos -> {
                _uiState.update { it.copy(nbPhotos = event.value) }
            }
            is SearchEvent.Available -> {
                _uiState.update { it.copy(available = event.value) }
            }
            is SearchEvent.City -> {
                _uiState.update { it.copy(city = event.value) }
            }
            is SearchEvent.Country -> {
                _uiState.update { it.copy(country = event.value) }
            }
            is SearchEvent.Extras -> {
                _uiState.update { it.copy(extras = event.value) }
            }
            is SearchEvent.MaxPrice -> {
                _uiState.update { it.copy(maxPrice = event.value) }
            }
            is SearchEvent.MaxSurface -> {
                _uiState.update { it.copy(maxSurface = event.value) }
            }
            is SearchEvent.MinPrice -> {
                _uiState.update { it.copy(minPrice = event.value) }
            }
            is SearchEvent.MinSurface -> {
                _uiState.update { it.copy(minSurface = event.value) }
            }
            is SearchEvent.Nearest -> {
                _uiState.update { it.copy(nearest = event.value) }
            }
            is SearchEvent.PostCode -> {
                _uiState.update { it.copy(postCode = event.value) }
            }
            is SearchEvent.State -> {
                _uiState.update { it.copy(state = event.value) }
            }
            is SearchEvent.Street -> {
                _uiState.update { it.copy(street = event.value) }
            }
            is SearchEvent.Type -> {
                _uiState.update { it.copy(type = event.value) }
            }
            is SearchEvent.NumberTime -> {
                _uiState.update { it.copy(nbTime = event.value) }
            }
            is SearchEvent.UnitTime -> {
                _uiState.update { it.copy(unitTime = event.value) }
            }
            SearchEvent.OnSearch -> {
                _uiState.update {
                    it.copy(
                        error = it.error.copy(
                            minPrice = if (it.minPrice.isBlank() && it.maxPrice.isNotBlank()) "Minimum price is Empty, Enter a number" else null,
                            maxPrice = if (it.minPrice.isNotBlank() && it.maxPrice.isBlank()) "Maximum price is Empty, Enter a number" else null,
                            minSurface = if (it.minSurface.isBlank() && it.maxSurface.isNotBlank()) "Minimum surface is Empty, Enter a number" else null,
                            maxSurface = if (it.minSurface.isNotBlank() && it.maxSurface.isBlank()) "Maximum surface is Empty, Enter a number" else null,
                            nbTime = if (it.nbTime.isBlank() && it.unitTime.isNotBlank()) "Number is Empty, Enter a number for the period" else null,
                            unitTime = if (it.unitTime.isBlank() && it.nbTime.isNotBlank()) "Unit time is empty, Choose one" else null,
                            city = if (it.city.isBlank() && it.country.isNotBlank() && it.extras.isNotBlank() && it.postCode.isNotBlank() && it.state.isNotBlank() && it.street.isNotBlank()) "City price is Empty, Enter a city" else null,
                            country = if (it.country.isBlank() && it.city.isNotBlank() && it.extras.isNotBlank() && it.postCode.isNotBlank() && it.state.isNotBlank() && it.street.isNotBlank()) "Country price is Empty, Enter a number" else null,
                            //extras = if (it.extras.isBlank() && it.city.isNotBlank() && it.country.isNotBlank() && it.postCode.isNotBlank() && it.state.isNotBlank() && it.street.isNotBlank()) "Extras price is Empty, Enter a number" else null,
                            postCode = if (it.postCode.isBlank() && it.city.isNotBlank() && it.country.isNotBlank() && it.extras.isNotBlank() && it.state.isNotBlank() && it.street.isNotBlank()) "PostCode price is Empty, Enter a number" else null,
                            state = if (it.state.isBlank() && it.city.isNotBlank() && it.country.isNotBlank() && it.extras.isNotBlank() && it.postCode.isNotBlank() && it.street.isNotBlank()) "State price is Empty, Enter a number" else null,
                            street = if (it.street.isBlank() && it.city.isNotBlank() && it.country.isNotBlank() && it.extras.isNotBlank() && it.postCode.isNotBlank() && it.state.isNotBlank()) "Street price is Empty, Enter a number" else null,
                        )
                    )
                }

                if (uiState.value.error != SearchError(null, null, null, null, null, null)) return

                search()
            }
        }
    }

    private fun search() {
        val address = uiState.value.let {
            Address(
                street = it.street,
                extras = it.extras,
                state = it.state,
                city = it.city,
                postCode = it.postCode,
                country = it.country
            )
        }

        val period =
            if (uiState.value.unitTime.isBlank() && uiState.value.nbTime.isBlank()) LocalDate.parse("1970-01-01")
            else calculatePeriod(uiState.value.unitTime.toLong(), uiState.value.nbTime)
        val release = if (uiState.value.available.not()) period else LocalDate.parse("1970-01-01")

        val newSearchQuery = SearchQuery(
            type = uiState.value.type,
            zone = address.city,
            minPrice = uiState.value.minPrice.toFloatOrNull() ?: 0f,
            maxPrice = uiState.value.maxPrice.toFloatOrNull() ?: Float.MAX_VALUE,
            release = release,
            status = uiState.value.available,
            minSurface = uiState.value.minSurface.toIntOrNull() ?: 0,
            maxSurface = uiState.value.maxSurface.toIntOrNull() ?: Int.MAX_VALUE,
            nearest = uiState.value.nearest,
            nbPhotos = uiState.value.nbPhotos.filter { it.isDigit() }.toIntOrNull() ?: 1
        )

        search(newSearchQuery)
    }

    private fun search(searchQuery: SearchQuery) {
        searchQueryRepository.setCurrent(searchQuery)
    }
}