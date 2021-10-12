package com.sursulet.realestatemanager.ui.main

import com.google.common.truth.Truth.assertThat
import com.sursulet.realestatemanager.MainCoroutineRule
import com.sursulet.realestatemanager.data.local.model.Address
import com.sursulet.realestatemanager.data.local.model.Photo
import com.sursulet.realestatemanager.data.local.model.RealEstate
import com.sursulet.realestatemanager.data.local.model.RealEstateWithPhotos
import com.sursulet.realestatemanager.repository.RealEstateRepository
import com.sursulet.realestatemanager.repository.shared.RealEstateIdRepository
import com.sursulet.realestatemanager.repository.shared.SearchQueryRepository
import com.sursulet.realestatemanager.ui.adapters.RealEstateUiModel
import com.sursulet.realestatemanager.ui.search.SearchQuery
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
import java.time.LocalDate

@ExperimentalCoroutinesApi
class ListViewModelTest {

    @get:Rule
    var rule = MainCoroutineRule()

    private lateinit var viewModel: ListViewModel

    private val searchQueryRepository = mockkClass(SearchQueryRepository::class)
    private val realEstateIdRepository = mockkClass(RealEstateIdRepository::class)
    private val realEstateRepository = mockkClass(RealEstateRepository::class)

    private val estate = RealEstate(
        id = 1,
        type = "Duplex",
        price = 150000f,
        surface = 25.0,
        rooms = 3,
        bathrooms = 1,
        bedrooms = 0,
        description = "Description",
        address = Address(
            street = "40 Boulevard Haussmann",
            postCode = "75009",
            city = "Paris",
            country = "France",
            state = ""
        ),
        agent = "Peach"
    )

    private val estate2 = RealEstate(
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
            country = "France"
        ),
        sold = LocalDate.parse("2021-06-06"),
        agent = "Peach"
    )

    private val photos = listOf(
        Photo(id = 1, title = "Salon", image = mockk(), realEstateId = 1),
        Photo(id = 2, title = "Bathroom", image = mockk(), realEstateId = 1)
    )
    private val photos2 =
        listOf(Photo(id = 3, title = "Kitchen", image = mockk(), realEstateId = 2))

    private val search = SearchQuery(
        type = "Duplex",
        zone = "Paris",
        minPrice = 0f,
        maxPrice = Float.MAX_VALUE,
        nearest = "",
        minSurface = 0,
        maxSurface = Int.MAX_VALUE
    )

    private val uiModel = listOf(
        RealEstateWithPhotos(estate, photos),
        RealEstateWithPhotos(estate2, photos2)
    ).map {
        RealEstateUiModel(
            id = it.realEstate.id,
            image = it.photos[0].image,
            type = it.realEstate.type,
            city = it.realEstate.address.city,
            price = it.realEstate.price.toString()
        )
    }

    @Before
    fun setUp() {

        every { searchQueryRepository.searchQuery } returns MutableStateFlow(SearchQuery())
        every { realEstateIdRepository.setValue(1) } returns Unit
        every { realEstateRepository.getRealEstatesWithPhotos() } returns flowOf(
            listOf(
                RealEstateWithPhotos(estate, photos),
                RealEstateWithPhotos(estate2, photos2)
            )
        )
        viewModel = ListViewModel(
            rule.dispatcher,
            searchQueryRepository,
            realEstateRepository,
            realEstateIdRepository
        )
    }

    @Test
    fun `Get TwoPane`() = rule.runBlockingTest {
        viewModel.onEvent(ListEvent.TwoPane(true))

        val result = viewModel.uiState.value

        assertThat(result).isEqualTo(ListState(isTwoPane = true, list = uiModel))
    }

    @Test
    fun `Display estates when no searchQuery`() = rule.runBlockingTest {
        viewModel.onEvent(ListEvent.TwoPane(true))

        val result = viewModel.uiState.value

        assertThat(result).isEqualTo(ListState(isTwoPane = true, list = uiModel))
    }

    @Test
    fun `Display estates when selectedId`() = rule.runBlockingTest {
        viewModel.onEvent(ListEvent.SelectedId(1))

        val result = viewModel.uiState.value

        assertThat(result).isEqualTo(ListState(list = uiModel, selectedId = 1))
    }

    @Test
    fun `Display estates when search type Duplex`() = rule.runBlockingTest {
        every { searchQueryRepository.searchQuery } returns MutableStateFlow(search)
        every {
            realEstateRepository.search(
                "Duplex",
                "Paris",
                0.0f,
                3.4028235E38f,
                true,
                LocalDate.parse("1970-01-01"),
                0,
                2147483647,
                "",
                1
            )
        } returns flowOf(listOf(RealEstateWithPhotos(estate, photos)))

        viewModel = ListViewModel(
            rule.dispatcher,
            searchQueryRepository,
            realEstateRepository,
            realEstateIdRepository
        )

        val result = viewModel.uiState.value

        assertThat(result).isEqualTo(ListState(list = listOf(uiModel[0])))
    }
}