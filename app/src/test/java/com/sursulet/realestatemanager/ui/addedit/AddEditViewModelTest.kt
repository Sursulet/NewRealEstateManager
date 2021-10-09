package com.sursulet.realestatemanager.ui.addedit

import com.google.common.truth.Truth.assertThat
import com.sursulet.realestatemanager.MainCoroutineRule
import com.sursulet.realestatemanager.data.local.model.Address
import com.sursulet.realestatemanager.data.local.model.RealEstate
import com.sursulet.realestatemanager.repository.RealEstateRepository
import com.sursulet.realestatemanager.repository.shared.RealEstateIdRepository
import com.sursulet.realestatemanager.repository.shared.TwoPaneRepository
import io.mockk.every
import io.mockk.mockkClass
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

@ExperimentalCoroutinesApi
class AddEditViewModelTest {

    @get:Rule
    var rule = MainCoroutineRule()

    private lateinit var viewModel: AddEditViewModel
    private val twoPaneRepository = mockkClass(TwoPaneRepository::class)
    private val idRepository = mockkClass(RealEstateIdRepository::class)
    private val realEstateRepository = mockkClass(RealEstateRepository::class)

    @Before
    fun setUp() {}

    @Test
    fun `insert Real Estate with empty fields returns error messages in Add Mode`() =
        rule.runBlockingTest {
            every { idRepository.realEstateId } returns MutableStateFlow(null)
            every { realEstateRepository.getRealEstate(any()) } returns flowOf()
            every { twoPaneRepository.twoPane } returns MutableStateFlow(false)

            viewModel = AddEditViewModel(
                dispatcher = rule.dispatcher,
                twoPaneRepository = twoPaneRepository,
                realEstateIdRepository = idRepository,
                realEstateRepository = realEstateRepository
            )

            viewModel.onEvent(AddEditEvent.OnSave)

            val result = viewModel.uiState.value

            assertThat(result.title).isEqualTo("Add Real Estate")
            assertThat(result.error).isNotNull()
            assertThat(result.error.type).isEqualTo("You must enter a type")
            assertThat(result.error.price).isEqualTo("You must enter a price")
            assertThat(result.error.surface).isEqualTo("You must enter a surface")
            assertThat(result.error.rooms).isEqualTo("You must enter a rooms")
            assertThat(result.error.description).isEqualTo("You must enter a description")
            assertThat(result.error.street).isEqualTo("You must enter a street")
            assertThat(result.error.city).isEqualTo("You must enter a city")
            assertThat(result.error.state).isEqualTo("You must enter a state")
            assertThat(result.error.postCode).isEqualTo("You must enter a post code")
            assertThat(result.error.country).isEqualTo("You must enter a country")
            assertThat(result.error.agent).isEqualTo("You must enter a agent")
        }

    @Test
    fun `insert Real Estate with empty fields returns error messages in Edit Mode`() =
        rule.runBlockingTest {
            val estate = RealEstate(
                1,
                "",
                123000000.0f,
                690.0,
                rooms = 7,
                0,
                0,
                "",
                Address(street = "", null, "", "", "", "France"),
                null,
                true,
                LocalDate.now(),
                null,
                "Peach"
            )

            every { idRepository.realEstateId } returns MutableStateFlow(1)
            every { realEstateRepository.getRealEstate(any()) } returns flowOf(estate)
            every { twoPaneRepository.twoPane } returns MutableStateFlow(false)

            viewModel = AddEditViewModel(
                dispatcher = rule.dispatcher,
                twoPaneRepository,
                realEstateIdRepository = idRepository,
                realEstateRepository = realEstateRepository
            )

            viewModel.onEvent(AddEditEvent.OnSave)

            val result = viewModel.uiState.value

            assertThat(result.title).isEqualTo("Edit Real Estate")
            assertThat(result.error).isNotNull()
            assertThat(result.error.type).isEqualTo("You must enter a type")
            assertThat(result.error.description).isEqualTo("You must enter a description")
            assertThat(result.error.street).isEqualTo("You must enter a street")
            assertThat(result.error.city).isEqualTo("You must enter a city")
            assertThat(result.error.state).isEqualTo("You must enter a state")
            assertThat(result.error.postCode).isEqualTo("You must enter a post code")
            assertThat(result.error.price).isEqualTo(null)
            assertThat(result.error.surface).isEqualTo(null)
            assertThat(result.error.rooms).isEqualTo("You must enter a rooms")
            assertThat(result.error.country).isEqualTo("You must enter a country")
            assertThat(result.error.agent).isEqualTo("You must enter a agent")
        }
}