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
            assertThat(result.error.postCode).isEqualTo("You must enter a post code")
            assertThat(result.error.country).isEqualTo("You must enter a country")
            assertThat(result.error.agent).isEqualTo("You must enter a agent")
        }

    @Test
    fun `insert Real Estate with empty fields returns error messages in Edit Mode`() =
        rule.runBlockingTest {
            val estate = RealEstate(
                id = 1,
                type = "Flat",
                price = 123000000.0f,
                surface = 690.0,
                rooms = 7,
                description = "",
                address = Address(street = "", postCode = "", city = "", country = "France"),
                isAvailable = true,
                created = LocalDate.now(),
                agent= "Peach"
            )

            every { twoPaneRepository.twoPane } returns MutableStateFlow(false)
            every { idRepository.realEstateId } returns MutableStateFlow(1)
            every { realEstateRepository.getRealEstate(any()) } returns flowOf(estate)

            viewModel = AddEditViewModel(
                dispatcher = rule.dispatcher,
                twoPaneRepository,
                realEstateIdRepository = idRepository,
                realEstateRepository = realEstateRepository
            )

            viewModel.onEvent(AddEditEvent.Type(""))
            viewModel.onEvent(AddEditEvent.Rooms(""))
            viewModel.onEvent(AddEditEvent.Country(""))
            viewModel.onEvent(AddEditEvent.Agent(""))
            viewModel.onEvent(AddEditEvent.OnSave)

            val result = viewModel.uiState.value

            assertThat(result.title).isEqualTo("Edit Real Estate")
            assertThat(result.error).isNotNull()
            assertThat(result.error.type).isEqualTo("You must enter a type")
            assertThat(result.error.description).isEqualTo("You must enter a description")
            assertThat(result.error.street).isEqualTo("You must enter a street")
            assertThat(result.error.city).isEqualTo("You must enter a city")
            assertThat(result.error.postCode).isEqualTo("You must enter a post code")
            assertThat(result.error.price).isEqualTo(null)
            assertThat(result.error.surface).isEqualTo(null)
            assertThat(result.error.rooms).isEqualTo("You must enter a rooms")
            assertThat(result.error.country).isEqualTo("You must enter a country")
            assertThat(result.error.agent).isEqualTo("You must enter a agent")
        }
}