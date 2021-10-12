package com.sursulet.realestatemanager.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sursulet.realestatemanager.di.IoDispatcher
import com.sursulet.realestatemanager.repository.shared.SearchQueryRepository
import com.sursulet.realestatemanager.repository.shared.TwoPaneRepository
import com.sursulet.realestatemanager.utils.Others.calculatePeriod
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val twoPaneRepository: TwoPaneRepository,
    private val searchQueryRepository: SearchQueryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchState())
    val uiState = _uiState.asStateFlow()

    private val _navigation = Channel<SearchNavigation>(capacity = Channel.CONFLATED)
    val navigation = _navigation.receiveAsFlow()
        .shareIn(viewModelScope.plus(dispatcher), SharingStarted.WhileSubscribed())

    init {
        viewModelScope.launch {
            twoPaneRepository.twoPane.collect { twoPane ->
                _uiState.update { it.copy(isTwoPane = twoPane) }
            }
        }
    }

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.NbPhotos -> {
                _uiState.update { it.copy(nbPhotos = event.value) }
            }
            is SearchEvent.Available -> {
                _uiState.update { it.copy(available = event.value) }
                getDate(
                    number = uiState.value.nbTime,
                    period = uiState.value.unitTime,
                    available = event.value
                )
            }
            is SearchEvent.City -> {
                _uiState.update { it.copy(city = event.value) }
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
            is SearchEvent.Type -> {
                _uiState.update { it.copy(type = event.value) }
            }
            is SearchEvent.NumberTime -> {
                getDate(
                    number = event.value,
                    period = uiState.value.unitTime,
                    available = uiState.value.available
                )
                _uiState.update { it.copy(nbTime = event.value) }
            }
            is SearchEvent.UnitTime -> {
                getDate(
                    period = event.value,
                    number = uiState.value.nbTime,
                    available = uiState.value.available
                )
                _uiState.update { it.copy(unitTime = event.value) }
            }
            SearchEvent.OnSearch -> {
                _uiState.update {
                    it.copy(
                        error = it.error.copy(
                            nbTime = if (it.nbTime.isBlank() && it.unitTime.isNotBlank()) "Number is Empty, Enter a number for the period" else null,
                            unitTime = if (it.unitTime.isBlank() && it.nbTime.isNotBlank()) "Unit time is empty, Choose one" else null
                        )
                    )
                }

                if (uiState.value.error != SearchError(null, null)) return

                search()
            }
        }
    }

    private fun search() {

        val period =
            if (uiState.value.unitTime.isBlank() && uiState.value.nbTime.isBlank()) LocalDate.parse("1970-01-01")
            else calculatePeriod(uiState.value.nbTime.toLong(), uiState.value.unitTime)

        val newSearchQuery = SearchQuery(
            type = uiState.value.type,
            zone = uiState.value.city,
            minPrice = uiState.value.minPrice.toFloatOrNull() ?: 0f,
            maxPrice = uiState.value.maxPrice.toFloatOrNull() ?: Float.MAX_VALUE,
            isAvailable = uiState.value.available,
            date = period,
            minSurface = uiState.value.minSurface.toIntOrNull() ?: 0,
            maxSurface = uiState.value.maxSurface.toIntOrNull() ?: Int.MAX_VALUE,
            nearest = uiState.value.nearest,
            nbPhotos = uiState.value.nbPhotos.filter { it.isDigit() }.toIntOrNull() ?: 1
        )

        searchQueryRepository.setValue(newSearchQuery)

        if (uiState.value.isTwoPane) {
            _navigation.trySend(SearchNavigation.MainFragment)
        } else {
            _navigation.trySend(SearchNavigation.MainActivity)
        }
    }

    private fun getDate(period: String, number: String, available: Boolean) {

        if (period.isBlank() || number.isBlank()) {
            _uiState.update { it.copy(phrase = if (available) "All Properties available" else "All Properties sold") }
            return
        }
        if (period.isNotBlank() && number.isNotBlank()) {
            val date = calculatePeriod(number = number.toLong(), time = period)
            _uiState.update { it.copy(phrase = if (available) "Properties available since $date" else "Property sold since $date") }
        }
    }
}