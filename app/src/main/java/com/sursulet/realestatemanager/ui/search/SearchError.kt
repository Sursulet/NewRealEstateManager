package com.sursulet.realestatemanager.ui.search

data class SearchError(
    val minPrice: String? = null,
    val maxPrice: String? = null,
    val minSurface: String? = null,
    val maxSurface: String? = null,
    val nbTime: String? = null,
    val unitTime: String? = null,
    val city: String? = null,
    val country: String? = null,
    val extras: String? = null,
    val postCode: String? = null,
    val state: String? = null,
    val street: String? = null
)
