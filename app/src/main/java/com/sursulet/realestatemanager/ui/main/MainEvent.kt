package com.sursulet.realestatemanager.ui.main

sealed class MainEvent {
    object TwoPaneScreen : MainEvent()
    data class Detail(val id: Long) : MainEvent()
    object Add : MainEvent()
    object Edit : MainEvent()
    object Search : MainEvent()
    object Maps : MainEvent()
    object Loan : MainEvent()
}
