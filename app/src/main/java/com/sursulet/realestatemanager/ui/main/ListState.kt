package com.sursulet.realestatemanager.ui.main

import com.sursulet.realestatemanager.ui.adapters.RealEstateUiModel

data class ListState(
    val isTwoPane: Boolean = false,
    val list: List<RealEstateUiModel> = emptyList(),
    val selectedId: Long = -1,
    val searchQuery: String = ""
)
