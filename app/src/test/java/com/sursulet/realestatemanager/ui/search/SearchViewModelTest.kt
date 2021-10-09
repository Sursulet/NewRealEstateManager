package com.sursulet.realestatemanager.ui.search

import com.google.common.truth.Truth.assertThat
import com.sursulet.realestatemanager.MainCoroutineRule
import com.sursulet.realestatemanager.repository.shared.SearchQueryRepository
import io.mockk.mockkClass
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchViewModelTest {

    @get:Rule
    var rule = MainCoroutineRule()

    private lateinit var viewModel: SearchViewModel

    private val searchQueryRepository = mockkClass(SearchQueryRepository::class)

    @Before
    fun setUp() {
        viewModel = SearchViewModel(searchQueryRepository)
    }

    @Test
    fun `Display error message for minPrice when minPrice is empty and maxPrice is not`() = rule.runBlockingTest {

        viewModel.onEvent(SearchEvent.MaxPrice("170000"))
        viewModel.onEvent(SearchEvent.OnSearch)
        val result = viewModel.uiState.value

        assertThat(result).isEqualTo(SearchState(maxPrice = "170000", error = SearchError(minPrice = "Minimum price is Empty, Enter a number")))
    }

    @Test
    fun `Display error message for maxPrice when maxPrice is empty and minPrice is not`() = rule.runBlockingTest {

        viewModel.onEvent(SearchEvent.MinPrice("110000"))
        viewModel.onEvent(SearchEvent.OnSearch)
        val result = viewModel.uiState.value

        assertThat(result).isEqualTo(SearchState(minPrice = "110000", error = SearchError(maxPrice = "Maximum price is Empty, Enter a number")))
    }

    @Test
    fun `Display error message for minSurface when minSurface is empty and maxSurface is not`() = rule.runBlockingTest {

        viewModel.onEvent(SearchEvent.MaxSurface("750"))
        viewModel.onEvent(SearchEvent.OnSearch)
        val result = viewModel.uiState.value

        assertThat(result).isEqualTo(SearchState(maxSurface = "750", error = SearchError(minSurface = "Minimum surface is Empty, Enter a number")))
    }

    @Test
    fun `Display error message for maxSurface when maxSurface is empty and minSurface is not`() = rule.runBlockingTest {

        viewModel.onEvent(SearchEvent.MinSurface("25"))
        viewModel.onEvent(SearchEvent.OnSearch)
        val result = viewModel.uiState.value

        assertThat(result).isEqualTo(SearchState(minSurface = "25", error = SearchError(maxSurface = "Maximum surface is Empty, Enter a number")))
    }

    @Test
    fun `Display error message for number when number is empty and unit is not`() = rule.runBlockingTest {

        viewModel.onEvent(SearchEvent.UnitTime("11"))
        viewModel.onEvent(SearchEvent.OnSearch)
        val result = viewModel.uiState.value

        assertThat(result).isEqualTo(SearchState(unitTime = "11", error = SearchError(nbTime = "Number is Empty, Enter a number for the period")))
    }

    @Test
    fun `Display error message for unit when unit is empty and number is not`() = rule.runBlockingTest {

        viewModel.onEvent(SearchEvent.NumberTime("15"))
        viewModel.onEvent(SearchEvent.OnSearch)
        val result = viewModel.uiState.value

        assertThat(result).isEqualTo(SearchState(nbTime = "15", error = SearchError(unitTime = "Unit time is empty, Choose one")))
    }
}