package com.sursulet.realestatemanager.ui.main

import com.google.common.truth.Truth.assertThat
import com.sursulet.realestatemanager.MainCoroutineRule
import com.sursulet.realestatemanager.repository.NetworkRepository
import com.sursulet.realestatemanager.repository.shared.RealEstateIdRepository
import com.sursulet.realestatemanager.repository.shared.TwoPaneRepository
import io.mockk.every
import io.mockk.mockkClass
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    var rule = MainCoroutineRule()

    private lateinit var viewModel: MainViewModel
    private val twoPaneRepository = mockkClass(TwoPaneRepository::class)
    private val networkRepository = mockkClass(NetworkRepository::class)
    private val realEstateIdRepository = mockkClass(RealEstateIdRepository::class)

    @Before
    fun setUp() {
        every { networkRepository.connectivityFlow() } returns flowOf(false)
        viewModel = MainViewModel(rule.dispatcher, twoPaneRepository, networkRepository, realEstateIdRepository)
    }

    @Test
    fun `when TwoPaneScreen returns true`() = rule.runBlockingTest {
        viewModel.onEvent(MainEvent.TwoPaneScreen)

        val state = viewModel.uiState.value

        assertThat(state).isEqualTo(MainState(isTwoPane = true))
    }

    @Test
    fun `Navigate to DetailActivity when click on detail and returns detail visibility true`() =
        rule.runBlockingTest {

            viewModel.onEvent(MainEvent.Detail(1))

            val result = viewModel.uiState.value
            val action = viewModel.navigation.first()

            assertThat(result).isEqualTo(MainState(isDetailVisible = true))
            assertThat(action).isEqualTo(MainNavigation.DetailActivity)
        }

    @Test
    fun `Navigate to AddActivity when click on Add`() = rule.runBlockingTest {

        viewModel.onEvent(MainEvent.Add)

        val result = viewModel.uiState.value
        val action = viewModel.navigation.first()

        assertThat(result).isEqualTo(MainState())
        assertThat(action).isEqualTo(MainNavigation.AddEditActivity)
    }

    @Test
    fun `Navigate to AddFragment from TwoPaneScreen when click on Add`() = rule.runBlockingTest {

        viewModel.onEvent(MainEvent.TwoPaneScreen)
        viewModel.onEvent(MainEvent.Add)

        val result = viewModel.uiState.value
        val action = viewModel.navigation.first()

        assertThat(result).isEqualTo(MainState(isTwoPane = true))
        assertThat(action).isEqualTo(MainNavigation.AddEditFragment)
    }

    @Test
    fun `Display a error message when click on Edit without being in TwoPaneScreen mode`() =
        rule.runBlockingTest {

            viewModel.onEvent(MainEvent.Edit)

            val result = viewModel.uiState.value
            val action = viewModel.navigation.first()

            assertThat(result).isEqualTo(MainState())
            assertThat(action).isEqualTo(MainNavigation.Message("No Real Estate has been chosen"))
        }

    @Test
    fun `Navigate to EditFragment from DetailFragment with TwoPaneScreen mode when click on Edit`() =
        rule.runBlockingTest {

            viewModel.onEvent(MainEvent.TwoPaneScreen)
            viewModel.onEvent(MainEvent.Detail(1))
            viewModel.onEvent(MainEvent.Edit)

            val result = viewModel.uiState.value
            val action = viewModel.navigation.first()

            assertThat(result).isEqualTo(MainState(isTwoPane = true))
            assertThat(action).isEqualTo(MainNavigation.AddEditFragment)
        }

    @Test
    fun `Display error message with DetailFragment not visible with TwoPaneScreen mode when click on Edit`() =
        rule.runBlockingTest {

            viewModel.onEvent(MainEvent.TwoPaneScreen)
            viewModel.onEvent(MainEvent.Edit)

            val result = viewModel.uiState.value
            val action = viewModel.navigation.first()

            assertThat(result).isEqualTo(MainState(isTwoPane = true))
            assertThat(action).isEqualTo(MainNavigation.Message("No Real Estate has been chosen"))
        }

    @Test
    fun `Navigate to SearchActivity when click on search`() =
        rule.runBlockingTest {

            viewModel.onEvent(MainEvent.Search)

            val result = viewModel.uiState.value
            val action = viewModel.navigation.first()

            assertThat(result).isEqualTo(MainState())
            assertThat(action).isEqualTo(MainNavigation.SearchActivity)
        }

    @Test
    fun `Navigate to SearchFragment from TwoPaneScreen mode when click on search`() =
        rule.runBlockingTest {

            viewModel.onEvent(MainEvent.TwoPaneScreen)
            viewModel.onEvent(MainEvent.Search)

            val result = viewModel.uiState.value
            val action = viewModel.navigation.first()

            assertThat(result).isEqualTo(MainState(isTwoPane = true))
            assertThat(action).isEqualTo(MainNavigation.SearchFragment)
        }

    @Test
    fun `Navigate to MapsActivity when network is connected`() = rule.runBlockingTest {
        every { networkRepository.connectivityFlow() } returns flowOf(true)
        viewModel = MainViewModel(rule.dispatcher, twoPaneRepository, networkRepository, realEstateIdRepository)

        viewModel.onEvent(MainEvent.Maps)

        val state = viewModel.uiState.value
        val action = viewModel.navigation.first()

        assertThat(state).isEqualTo(MainState(isConnected = true))
        assertThat(action).isEqualTo(MainNavigation.MapsActivity)
    }

    @Test
    fun `Navigate to MapsFragment from TwoPaneScreen when network is connected`() =
        rule.runBlockingTest {
            every { networkRepository.connectivityFlow() } returns flowOf(true)
            viewModel = MainViewModel(rule.dispatcher, twoPaneRepository, networkRepository, realEstateIdRepository)

            viewModel.onEvent(MainEvent.TwoPaneScreen)
            viewModel.onEvent(MainEvent.Maps)

            val state = viewModel.uiState.value
            val action = viewModel.navigation.first()

            assertThat(state).isEqualTo(MainState(isTwoPane = true, isConnected = true))
            assertThat(action).isEqualTo(MainNavigation.MapsFragment)
        }

    @Test
    fun `Display a error message when network is not connected returns false `() =
        rule.runBlockingTest {
            every { networkRepository.connectivityFlow() } returns flowOf(false)
            viewModel = MainViewModel(rule.dispatcher, twoPaneRepository, networkRepository, realEstateIdRepository)

            viewModel.onEvent(MainEvent.Maps)

            val state = viewModel.uiState.value
            val action = viewModel.navigation.first()

            assertThat(state).isEqualTo(MainState())
            assertThat(action).isEqualTo(MainNavigation.Message("You are not connected"))
        }

    @Test
    fun `Display a error message from TwoPaneScreen when network is not connected returns false `() =
        rule.runBlockingTest {
            every { networkRepository.connectivityFlow() } returns flowOf(false)
            viewModel = MainViewModel(rule.dispatcher, twoPaneRepository, networkRepository, realEstateIdRepository)

            viewModel.onEvent(MainEvent.TwoPaneScreen)
            viewModel.onEvent(MainEvent.Maps)

            val state = viewModel.uiState.value
            val action = viewModel.navigation.first()

            assertThat(state).isEqualTo(MainState(isTwoPane = true))
            assertThat(action).isEqualTo(MainNavigation.Message("You are not connected"))
        }

    @Test
    fun `Navigate to LoanActivity when click on Loan`() = rule.runBlockingTest {

        viewModel.onEvent(MainEvent.Loan)

        val state = viewModel.uiState.value
        val action = viewModel.navigation.first()

        assertThat(state).isEqualTo(MainState())
        assertThat(action).isEqualTo(MainNavigation.LoanActivity)
    }

    @Test
    fun `Navigate to LoanFragment from TwoPaneScreen when click on Loan`() = rule.runBlockingTest {

        viewModel.onEvent(MainEvent.TwoPaneScreen)
        viewModel.onEvent(MainEvent.Loan)

        val state = viewModel.uiState.value
        val action = viewModel.navigation.first()

        assertThat(state).isEqualTo(MainState(isTwoPane = true))
        assertThat(action).isEqualTo(MainNavigation.LoanFragment)
    }
}