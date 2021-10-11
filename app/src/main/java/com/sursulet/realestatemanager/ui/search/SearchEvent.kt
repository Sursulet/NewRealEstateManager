package com.sursulet.realestatemanager.ui.search

sealed class SearchEvent {
    data class NbPhotos(val value: String) : SearchEvent()
    data class Type(val value: String) : SearchEvent()
    data class Available(val value: Boolean) : SearchEvent()
    data class MaxPrice(val value: String) : SearchEvent()
    data class MinPrice(val value: String) : SearchEvent()
    data class MaxSurface(val value: String) : SearchEvent()
    data class MinSurface(val value: String) : SearchEvent()
    data class City(val value: String) : SearchEvent()
    data class Nearest(val value: String) : SearchEvent()
    data class NumberTime(val value: String) : SearchEvent()
    data class UnitTime(val value: String) : SearchEvent()
    object OnSearch : SearchEvent()
}
