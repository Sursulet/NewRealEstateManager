package com.sursulet.realestatemanager.ui.detail

import com.google.android.gms.maps.model.LatLng
import com.google.common.truth.Truth.assertThat
import com.sursulet.realestatemanager.MainCoroutineRule
import com.sursulet.realestatemanager.data.local.model.Address
import com.sursulet.realestatemanager.data.local.model.Photo
import com.sursulet.realestatemanager.data.local.model.RealEstate
import com.sursulet.realestatemanager.data.local.model.RealEstateWithPhotos
import com.sursulet.realestatemanager.repository.GeocoderRepository
import com.sursulet.realestatemanager.repository.RealEstateRepository
import com.sursulet.realestatemanager.repository.shared.RealEstateIdRepository
import com.sursulet.realestatemanager.ui.adapters.PhotoUiModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkClass
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DetailViewModelTest {

    @get:Rule
    var rule = MainCoroutineRule()

    private lateinit var viewModel: DetailViewModel
    private val realEstateIdRepository = mockkClass(RealEstateIdRepository::class)
    private val realEstateRepository = mockkClass(RealEstateRepository::class)
    private val geocoderRepository = mockkClass(GeocoderRepository::class)

    @Before
    fun setUp() {
        every { realEstateIdRepository.realEstateId } returns MutableStateFlow(1)
        every { realEstateRepository.getRealEstateWithPhotos(1) } returns flowOf(estateWithPhotos)

        viewModel = DetailViewModel(
            rule.dispatcher,
            realEstateIdRepository,
            realEstateRepository,
            geocoderRepository
        )
    }

    @Test
    fun getRealEstates() = rule.runBlockingTest {
        val result = viewModel.uiState.value

        assertThat(result).isEqualTo(
            DetailState(
                id = 2,
                media = uiModel,
                description = "Description",
                surface = "50.0",
                rooms = "4",
                bathrooms = "1",
                bedrooms = "1",
                location = "7 rue Linois, 75015 Paris, France",
                coordinates = LatLng(48.866667, 2.333333)
            )
        )
    }


    private val photos = listOf(Photo(id = 1, "Salon", mockk(), realEstateId = 2))

    private val estate = RealEstate(
        id = 2,
        type = "Penthouse",
        price = 230000f,
        surface = 50.0,
        rooms = 4,
        bathrooms = 1,
        bedrooms = 1,
        description = "Description",
        address = Address(
            street = "7 rue Linois",
            postCode = "75015",
            city = "Paris",
            country = "France",
            state = ""
        ),
        isAvailable = false,
        agent = "Peach"
    )
    private val estateWithPhotos = RealEstateWithPhotos(estate, photos)
    private val uiModel = photos.map { PhotoUiModel(it.id, it.title, it.image) }
}