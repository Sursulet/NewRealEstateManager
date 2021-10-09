package com.sursulet.realestatemanager.ui.loan

import androidx.lifecycle.ViewModel
import com.sursulet.realestatemanager.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.math.pow

@HiltViewModel
class LoanViewModel @Inject constructor(@IoDispatcher dispatcher: CoroutineDispatcher) : ViewModel() {

    private val _uiState = MutableStateFlow(LoanState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: LoanEvent) {
        when (event) {
            is LoanEvent.Contribution -> {
                _uiState.update { it.copy(contribution = event.value) }
            }
            is LoanEvent.Years -> {
                _uiState.update { it.copy(years = event.value) }
            }
            is LoanEvent.Rate -> {
                _uiState.update { it.copy(rate = event.value) }
            }
            LoanEvent.Calculate -> {

                _uiState.update {
                    it.copy(
                        error = LoanError(
                            contribution = if (it.contribution.isBlank()) "You must enter a contribution" else null,
                            years = if (it.years.isBlank()) "You must enter a number of years" else null,
                            rate = if (it.rate.isBlank()) "You must enter a rate" else null
                        )
                    )
                }

                if (uiState.value.error != LoanError(null, null, null)) return
                onCalculate()
            }
        }
    }

    private fun onCalculate() {

        val result: Double
        uiState.value.apply {
            result = contribution.toDouble()*(rate.toDouble()*(1+rate.toDouble()).pow(years.toInt())) / ((1+rate.toDouble()).pow(years.toInt())-1)
                //(contribution.toDouble() * rate.toDouble() / 12) / (1 - (1 + rate.toDouble() / 12).pow(-12 * years.toDouble()))
        }

        val resultString = "%.2f".format(result) //round(result)
        _uiState.update { it.copy(result = "$ $resultString per month") }
    }
}