package com.sursulet.realestatemanager.ui.loan

sealed class LoanEvent {
    data class Contribution(val value: String) : LoanEvent()
    data class Years(val value: String) : LoanEvent()
    data class Rate(val value: String) : LoanEvent()
    object Calculate : LoanEvent()
}
