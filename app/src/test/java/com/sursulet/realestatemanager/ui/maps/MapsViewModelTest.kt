package com.sursulet.realestatemanager.ui.maps

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.common.truth.Truth.assertThat
import com.sursulet.realestatemanager.MainCoroutineRule
import com.sursulet.realestatemanager.data.local.model.Address
import com.sursulet.realestatemanager.data.local.model.RealEstate
import com.sursulet.realestatemanager.repository.GeocoderRepository
import com.sursulet.realestatemanager.repository.RealEstateRepository
import io.mockk.every
import io.mockk.mockkClass
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MapsViewModelTest {

    @get:Rule
    var rule = MainCoroutineRule()

    private lateinit var viewModel: MapsViewModel
    private val client = mockkClass(FusedLocationProviderClient::class)
    private val geocoder = mockkClass(GeocoderRepository::class)
    private val realEstateRepository = mockkClass(RealEstateRepository::class)

    private val estate = RealEstate(
        id = 1,
        type = "Duplex",
        price = 150000f,
        surface = 70.0,
        rooms = 5,
        bathrooms = 2,
        bedrooms = 1,
        description = "Description",
        address = Address(
            street = "40 Boulevard Haussmann",
            postCode = "75009",
            city = "Paris",
            country = "France"
        ),
        nearest = "",
        isAvailable = true,
        agent = "PEACH"
    )

    @Before
    fun setUp() {
        every { realEstateRepository.getRealEstates() } returns flowOf(listOf(estate))
        viewModel = MapsViewModel(rule.dispatcher, client, geocoder, realEstateRepository)
    }

    @Test
    fun `Display estates when permission`() = rule.runBlockingTest {

        viewModel.onEvent(MapsEvent.MapReady)

        val result = viewModel.uiState.value

        assertThat(result).isEqualTo(MapsState(isMapReady = true))
    }
}