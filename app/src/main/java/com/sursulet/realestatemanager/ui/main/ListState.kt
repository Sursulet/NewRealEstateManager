package com.sursulet.realestatemanager.ui.main

import com.sursulet.realestatemanager.ui.adapters.RealEstateUiModel
import com.sursulet.realestatemanager.ui.search.SearchQuery

data class ListState(
    val isTwoPane: Boolean = false,
    val list: List<RealEstateUiModel> = emptyList(),
    val selectedId: Long? = null,
    val searchQuery: SearchQuery? = SearchQuery()
)
