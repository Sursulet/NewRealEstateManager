package com.sursulet.realestatemanager.ui.search

data class SearchState(
    val isTwoPane: Boolean = false,
    val type: String = "",
    val available: Boolean = true,
    val phrase: String = "All properties available",
    val minPrice: String = "",
    val maxPrice: String = "",
    val maxSurface: String = "",
    val minSurface: String = "",
    val nbTime: String = "",
    val unitTime: String = "",
    val nearest: String = "",
    val nbPhotos: String = "",
    val city: String = "",
    val country: String = "",
    val extras: String = "",
    val postCode: String = "",
    val state: String = "",
    val street: String = "",
    val error: SearchError = SearchError()
)
