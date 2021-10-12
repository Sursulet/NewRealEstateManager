package com.sursulet.realestatemanager.ui.search

import com.google.common.truth.Truth.assertThat
import com.sursulet.realestatemanager.MainCoroutineRule
import com.sursulet.realestatemanager.repository.shared.SearchQueryRepository
import com.sursulet.realestatemanager.repository.shared.TwoPaneRepository
import io.mockk.every
import io.mockk.mockkClass
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

@ExperimentalCoroutinesApi
class SearchViewModelTest {

    @get:Rule
    var rule = MainCoroutineRule()

    private lateinit var viewModel: SearchViewModel

    private val twoPaneRepository = mockkClass(TwoPaneRepository::class)
    private val searchQueryRepository = mockkClass(SearchQueryRepository::class)

    @Before
    fun setUp() {
        every { twoPaneRepository.twoPane } returns MutableStateFlow(false)
        viewModel = SearchViewModel(rule.dispatcher, twoPaneRepository, searchQueryRepository)
    }

    @Test
    fun `Display error message for number when number is empty and unit is not`() =
        rule.runBlockingTest {

            viewModel.onEvent(SearchEvent.UnitTime("11"))
            viewModel.onEvent(SearchEvent.OnSearch)
            val result = viewModel.uiState.value

            assertThat(result).isEqualTo(
                SearchState(
                    unitTime = "11",
                    error = SearchError(nbTime = "Number is Empty, Enter a number for the period"),
                    phrase = "All Properties available"
                )
            )
        }

    @Test
    fun `Display error message for unit when unit is empty and number is not`() =
        rule.runBlockingTest {

            viewModel.onEvent(SearchEvent.NumberTime("15"))
            viewModel.onEvent(SearchEvent.OnSearch)
            val result = viewModel.uiState.value

            assertThat(result).isEqualTo(
                SearchState(
                    nbTime = "15",
                    error = SearchError(unitTime = "Unit time is empty, Choose one"),
                    phrase = "All Properties available"
                )
            )
        }

    @Test
    fun `Display date when event is available `() = rule.runBlockingTest {

        viewModel.onEvent(SearchEvent.Available(true))
        viewModel.onEvent(SearchEvent.NumberTime("3"))
        viewModel.onEvent(SearchEvent.UnitTime("days"))

        val result = viewModel.uiState.value

        val date = LocalDate.now().minusDays(3)
        assertThat(result).isEqualTo(
            SearchState(
                available = true,
                nbTime = "3",
                unitTime = "days",
                phrase = "Properties available since $date"
            )
        )
    }
}