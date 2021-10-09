package com.sursulet.realestatemanager.ui.loan

import com.google.common.truth.Truth.assertThat
import com.sursulet.realestatemanager.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest

import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LoanViewModelTest {

    @get:Rule
    var rule = MainCoroutineRule()

    private lateinit var viewModel: LoanViewModel

    @Before
    fun setUp() {
        viewModel = LoanViewModel(rule.dispatcher)
    }

    @Test
    fun `Display a error message when Contribute field is empty`() = rule.runBlockingTest {

        viewModel.onEvent(LoanEvent.Contribution(""))
        viewModel.onEvent(LoanEvent.Years("20"))
        viewModel.onEvent(LoanEvent.Rate("0.8"))
        viewModel.onEvent(LoanEvent.Calculate)

        val result = viewModel.uiState.value

        assertThat(result).isEqualTo(
            LoanState(
                years = "20",
                rate = "0.8",
                error = LoanError(contribution = "You must enter a contribution")
            )
        )
    }

    @Test
    fun `Display a error message when Year field is empty`() = rule.runBlockingTest {

        viewModel.onEvent(LoanEvent.Contribution("150000"))
        viewModel.onEvent(LoanEvent.Years(""))
        viewModel.onEvent(LoanEvent.Rate("0.8"))
        viewModel.onEvent(LoanEvent.Calculate)

        val result = viewModel.uiState.value

        assertThat(result).isEqualTo(
            LoanState(
                contribution = "150000",
                rate = "0.8",
                error = LoanError(years = "You must enter a number of years")
            )
        )
    }

    @Test
    fun `Display a error message when Rate field is empty`() = rule.runBlockingTest {

        viewModel.onEvent(LoanEvent.Contribution("150000"))
        viewModel.onEvent(LoanEvent.Years("20"))
        viewModel.onEvent(LoanEvent.Rate(""))
        viewModel.onEvent(LoanEvent.Calculate)

        val result = viewModel.uiState.value

        assertThat(result).isEqualTo(
            LoanState(
                contribution = "150000",
                years = "20",
                error = LoanError(rate = "You must enter a rate")
            )
        )
    }

    @Test
    fun `Display result when click on Calculate`() = rule.runBlockingTest {

        viewModel.onEvent(LoanEvent.Contribution("10000"))
        viewModel.onEvent(LoanEvent.Years("60"))
        viewModel.onEvent(LoanEvent.Rate("0.00625"))
        viewModel.onEvent(LoanEvent.Calculate)

        val result = viewModel.uiState.value

        assertThat(result).isEqualTo(
            LoanState(
                result = "$ 200,38 per month",
                contribution = "10000",
                years = "60",
                rate = "0.00625"
            )
        )
    }
}