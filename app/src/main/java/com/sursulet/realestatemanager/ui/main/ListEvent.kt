package com.sursulet.realestatemanager.ui.main

sealed class ListEvent {
    data class TwoPane(val value: Boolean) : ListEvent()
    data class SelectedId(val value: Long) : ListEvent()
    object Search : ListEvent()
}
