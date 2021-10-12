package com.sursulet.realestatemanager.ui.main

import android.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sursulet.realestatemanager.R
import com.sursulet.realestatemanager.di.IoDispatcher
import com.sursulet.realestatemanager.repository.RealEstateRepository
import com.sursulet.realestatemanager.repository.shared.RealEstateIdRepository
import com.sursulet.realestatemanager.repository.shared.SearchQueryRepository
import com.sursulet.realestatemanager.ui.adapters.RealEstateUiModel
import com.sursulet.realestatemanager.ui.search.SearchQuery
import com.sursulet.realestatemanager.utils.Others.formattedPrice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class ListViewModel @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
    private val searchQueryRepository: SearchQueryRepository,
    private val realEstateRepository: RealEstateRepository,
    private val realEstateIdRepository: RealEstateIdRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch(dispatcher) {
            searchQueryRepository.searchQuery.flatMapLatest { query: SearchQuery ->
                //if (query != SearchQuery()) {

                    val search = realEstateRepository.search(
                        type = query.type,
                        city = query.zone,
                        minPrice = query.minPrice,
                        maxPrice = query.maxPrice,
                        isAvailable = query.isAvailable,
                        date = query.date,
                        minSurface = query.minSurface,
                        maxSurface = query.maxSurface,
                        nearest = query.nearest,
                        size = query.nbPhotos
                    )

                    _uiState.update { it.copy(searchQuery = query) }
                    search


                //} else {
                    //Log.d(TAG, "ListVM: No Query")
                    //_uiState.update { it.copy(searchQuery = null) }
                    //realEstateRepository.getRealEstatesWithPhotos().map { list ->
                    //    list.filter { it.photos != emptyList<Photo>() }
                    //}
                //}
            }.map { estates ->
                estates.map {
                    RealEstateUiModel(
                        it.realEstate.id,
                        it.photos[0].image,
                        it.realEstate.address.city,
                        it.realEstate.type,
                        formattedPrice(it.realEstate.price)
                    )
                }
            }.collect { state ->
                _uiState.update { it.copy(list = state) }
            }
        }
    }

    fun onEvent(event: ListEvent) {
        when (event) {
            is ListEvent.TwoPane -> {
                _uiState.update { it.copy(isTwoPane = event.value) }
            }
            is ListEvent.SelectedId -> {
                _uiState.update {
                    it.copy(
                        selectedId = event.value,
                        list = it.list.map { model ->
                            model.copy(
                                backgroundColorRes = if (event.value == model.id && it.isTwoPane) R.color.purple_500 else Color.WHITE,
                                textColor = if (event.value == model.id && it.isTwoPane) Color.WHITE else R.color.purple_500
                            )
                        }
                    )
                }

                realEstateIdRepository.setValue(event.value)
            }
            is ListEvent.Search -> {
                searchQueryRepository.setValue(SearchQuery())
                _uiState.update { it.copy(searchQuery = null) }
            }
        }
    }
}