package com.sursulet.realestatemanager.ui.search

import java.time.LocalDate

data class SearchQuery(
    val type: String,
    val zone: String,
    val minPrice: Float,
    val maxPrice: Float,
    var release: LocalDate = LocalDate.parse("1970-01-01"),
    val status: Boolean = true,
    val minSurface: Int,
    val maxSurface: Int,
    val nearest: String,
    val nbPhotos: Int = 1
)
