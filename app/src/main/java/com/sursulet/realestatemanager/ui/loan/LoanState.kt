package com.sursulet.realestatemanager.ui.loan

data class LoanState(
    val result: String = "000.0",
    val contribution: String = "",
    val years: String = "",
    val rate: String = "",
    val error: LoanError = LoanError()
)
