package com.sursulet.realestatemanager.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sursulet.realestatemanager.di.IoDispatcher
import com.sursulet.realestatemanager.repository.NetworkRepository
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
class MainViewModel @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val networkRepository: NetworkRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainState())
    val uiState = _uiState.asStateFlow()

    private val _navigation = Channel<MainNavigation>(capacity = Channel.CONFLATED)
    val navigation = _navigation.receiveAsFlow()
        .shareIn(viewModelScope.plus(dispatcher), SharingStarted.WhileSubscribed())

    init {
        viewModelScope.launch(dispatcher) {
            networkRepository.connectivityFlow().collect { connected ->
                _uiState.update { it.copy(isConnected = connected) }
            }
        }
    }

    fun onEvent(event: MainEvent) {
        when (event) {
            is MainEvent.TwoPaneScreen -> {
                _uiState.update { it.copy(isTwoPane = true) }
            }
            is MainEvent.Detail -> {
                _uiState.update { it.copy(isDetailVisible = true) }
                viewModelScope.launch {
                    if (uiState.value.isTwoPane) {
                        _navigation.trySend(MainNavigation.DetailFragment)
                    } else {
                        _navigation.trySend(MainNavigation.DetailActivity)
                    }
                }
            }
            is MainEvent.Add -> {
                _uiState.update { it.copy(isDetailVisible = false) }
                viewModelScope.launch {
                    if (uiState.value.isTwoPane) {
                        _navigation.trySend(MainNavigation.AddEditFragment)
                    } else {
                        _navigation.trySend(MainNavigation.AddEditActivity)
                    }
                }
            }
            is MainEvent.Edit -> {
                viewModelScope.launch {
                    if (uiState.value.isTwoPane) {
                        if (uiState.value.isDetailVisible) {
                            _navigation.trySend(MainNavigation.AddEditFragment)
                        } else {
                            _navigation.trySend(MainNavigation.Message("No Real Estate has been chosen"))
                        }
                    } else {
                        _navigation.trySend(MainNavigation.Message("No Real Estate has been chosen"))
                    }
                }
                _uiState.update { it.copy(isDetailVisible = false) }
            }
            is MainEvent.Search -> {
                _uiState.update { it.copy(isDetailVisible = false) }
                viewModelScope.launch {
                    if (uiState.value.isTwoPane) {
                        _navigation.trySend(MainNavigation.SearchFragment)
                    } else {
                        _navigation.trySend(MainNavigation.SearchActivity)
                    }
                }
            }
            is MainEvent.Maps -> {
                _uiState.update { it.copy(isDetailVisible = false) }
                viewModelScope.launch {
                    if (uiState.value.isConnected) {
                        if (uiState.value.isTwoPane) {
                            _navigation.trySend(MainNavigation.MapsFragment)
                        } else {
                            _navigation.trySend(MainNavigation.MapsActivity)
                        }
                    } else {
                        _navigation.trySend(MainNavigation.Message("You are not connected"))
                    }
                }
            }
            is MainEvent.Loan -> {
                _uiState.update { it.copy(isDetailVisible = false) }
                viewModelScope.launch {
                    if (uiState.value.isTwoPane) {
                        _navigation.trySend(MainNavigation.LoanFragment)
                    } else {
                        _navigation.trySend(MainNavigation.LoanActivity)
                    }
                }
            }
        }
    }
}