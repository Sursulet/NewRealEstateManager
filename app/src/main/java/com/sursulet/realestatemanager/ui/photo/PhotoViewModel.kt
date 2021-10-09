package com.sursulet.realestatemanager.ui.photo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sursulet.realestatemanager.data.local.model.Photo
import com.sursulet.realestatemanager.di.IoDispatcher
import com.sursulet.realestatemanager.repository.PhotoRepository
import com.sursulet.realestatemanager.repository.shared.LastPhotoRepository
import com.sursulet.realestatemanager.repository.shared.RealEstateIdRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val lastPhotoRepository: LastPhotoRepository,
    private val realEstateIdRepository: RealEstateIdRepository,
    private val photoRepository: PhotoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PhotoState())
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = Channel<String>(capacity = Channel.CONFLATED)
    val eventFlow = _eventFlow.receiveAsFlow()
        .shareIn(viewModelScope.plus(dispatcher), SharingStarted.WhileSubscribed())

    init {
        viewModelScope.launch {
            realEstateIdRepository.realEstateId.filterNotNull()
                .combine(lastPhotoRepository.photo) { id, photo ->
                    if (photo != null) {
                        PhotoState(
                            id = photo.id,
                            title = photo.title,
                            image = photo.image,
                            realEstateId = id
                        )
                    } else PhotoState(realEstateId = id)
                }.collect {
                    _uiState.value = it
                }
        }
    }

    fun onEvent(event: PhotoEvent) {
        when (event) {
            is PhotoEvent.Title -> _uiState.update { it.copy(title = event.value) }
            is PhotoEvent.Image -> _uiState.update { it.copy(image = event.value) }
            PhotoEvent.OnSave -> onSave()
        }
    }

    private fun onSave() {

        var hasError = false

        _uiState.update {
            it.copy(
                errorTitle = if (uiState.value.title.isBlank()) {
                    hasError = true
                    "You must enter a title"
                } else null
            )
        }

        if (uiState.value.image == null) {
            _eventFlow.trySend("You must enter a image")
        }

        if (hasError) return

        if (uiState.value.id != null) {
            val updatedPhoto =
                Photo(
                    id = uiState.value.id!!,
                    title = uiState.value.title,
                    image = uiState.value.image!!,
                    realEstateId = uiState.value.realEstateId
                )

            updatePhoto(updatedPhoto)
        } else {
            val newPhoto =
                Photo(
                    title = uiState.value.title,
                    image = uiState.value.image!!,
                    realEstateId = 2//uiState.value.realEstateId
                )
            createPhoto(newPhoto)
        }
    }

    private fun createPhoto(photo: Photo) = viewModelScope.launch {
        photoRepository.insert(photo)
        _eventFlow.trySend("Photo has been added to the gallery")
    }

    private fun updatePhoto(photo: Photo) = viewModelScope.launch(dispatcher) {
        photoRepository.update(photo)
        _eventFlow.trySend("Photo has been updated in the gallery")
    }

}