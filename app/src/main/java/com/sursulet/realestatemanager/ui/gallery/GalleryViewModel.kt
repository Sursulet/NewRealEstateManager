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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

@ExperimentalCoroutinesApi
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

    private val _navigation = Channel<GalleryNavigation>(capacity = Channel.CONFLATED)
    val navigation = _navigation.receiveAsFlow()
        .shareIn(viewModelScope.plus(dispatcher), SharingStarted.WhileSubscribed())


    init {

        viewModelScope.launch {
            realEstateIdRepository.realEstateId.filterNotNull().flatMapLatest { id ->
                photoRepository.getPhotos(id)
            }.combine(twoPaneRepository.twoPane) { photos, twoPane ->
                GalleryState(isTwoPane = twoPane, photos = photos.map {
                    PhotoUiModel(id = it.id, title = it.title, image = it.image)
                })
            }.collect {
                _uiState.value = it
            }
        }
    }

    fun onEvent(event: GalleryEvent) {
        when (event) {
            is GalleryEvent.OnEdit -> {
                lastPhotoRepository.setValue(event.photo)
            }
            is GalleryEvent.OnClose -> {
                if (uiState.value.photos.isEmpty()) _navigation.trySend(GalleryNavigation.Cancel)
                else _navigation.trySend(GalleryNavigation.CloseFragment)
            }
            GalleryEvent.OnSave -> {
                if (uiState.value.photos.isEmpty()) _navigation.trySend(GalleryNavigation.EmptyGallery())
                else {
                    if (uiState.value.isTwoPane) _navigation.trySend(GalleryNavigation.CloseFragment)
                    else _navigation.trySend(GalleryNavigation.CloseActivity)
                }
            }
        }
    }
}