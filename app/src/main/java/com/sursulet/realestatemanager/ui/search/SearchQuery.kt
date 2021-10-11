package com.sursulet.realestatemanager.ui.search

import java.time.LocalDate

data class SearchQuery(
    val type: String = "",
    val zone: String = "",
    val minPrice: Float = Float.MIN_VALUE,
    val maxPrice: Float = Float.MAX_VALUE,
    val isAvailable: Boolean = true,
    var date: LocalDate = LocalDate.parse("1970-01-01"),
    val minSurface: Int = Int.MIN_VALUE,
    val maxSurface: Int = Int.MAX_VALUE,
    val nearest: String = "",
    val nbPhotos: Int = 1
)
