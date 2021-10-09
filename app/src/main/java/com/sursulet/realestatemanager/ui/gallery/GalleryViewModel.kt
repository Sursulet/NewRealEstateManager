package com.sursulet.realestatemanager.ui.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sursulet.realestatemanager.di.IoDispatcher
import com.sursulet.realestatemanager.repository.PhotoRepository
import com.sursulet.realestatemanager.repository.shared.LastPhotoRepository
import com.sursulet.realestatemanager.repository.shared.RealEstateIdRepository
import com.sursulet.realestatemanager.repository.shared.TwoPaneRepository
import com.sursulet.realestatemanager.ui.adapters.PhotoUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
    private val twoPaneRepository: TwoPaneRepository,
    private val realEstateIdRepository: RealEstateIdRepository,
    private val photoRepository: PhotoRepository,
    private val lastPhotoRepository: LastPhotoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GalleryState())
    val uiState = _uiState.asStateFlow()

    private val _navigation = Channel<String>(capacity = Channel.CONFLATED)
    val navigation = _navigation.receiveAsFlow()
        .shareIn(viewModelScope.plus(dispatcher), SharingStarted.WhileSubscribed())

    init {
        viewModelScope.launch(dispatcher) {
            realEstateIdRepository.realEstateId.filterNotNull().flatMapLatest {
                photoRepository.getPhotos(it)
            }.map { photos ->
                photos.map {
                    PhotoUiModel(id = it.id, title = it.title, image = it.image)
                }
            }.combine(twoPaneRepository.twoPane) { photos, twoPane ->
                _uiState.update { it.copy(isTwoPane = twoPane, photos = photos) }
            }
        }
    }

    fun onEvent(event: GalleryEvent) {
        when (event) {
            is GalleryEvent.OnEdit -> {
                lastPhotoRepository.setValue(event.photo)
            }
            GalleryEvent.OnSave -> {
                /*
                if (uiState.value.isTwoPane) {
                    if (uiState.value.id != null) {
                        _navigation.trySend(GalleryNavigation.DetailFragment)
                    } else {
                        _navigation.trySend(GalleryNavigation.MainFragment)
                    }
                } else {
                    if (uiState.value.id != null) {
                        _navigation.trySend(GalleryNavigation.DetailActivity)
                    } else {
                        _navigation.trySend(GalleryNavigation.MainActivity)
                    }
                }

                 */
            }
        }
    }
}