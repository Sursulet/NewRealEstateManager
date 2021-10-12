package com.sursulet.realestatemanager.ui.addedit

sealed class AddEditEvent {
    data class Type(val value: String) : AddEditEvent()
    data class Price(val value: String) : AddEditEvent()
    data class Surface(val value: String) : AddEditEvent()
    data class Rooms(val value: String) : AddEditEvent()
    data class Bedrooms(val value: String) : AddEditEvent()
    data class Bathrooms(val value: String) : AddEditEvent()
    data class Description(val value: String) : AddEditEvent()
    data class Street(val value: String) : AddEditEvent()
    data class Extras(val value: String) : AddEditEvent()
    data class City(val value: String) : AddEditEvent()
    data class State(val value: String) : AddEditEvent()
    data class PostCode(val value: String) : AddEditEvent()
    data class Country(val value: String) : AddEditEvent()
    data class Nearest(val value: String) : AddEditEvent()
    data class IsAvailable(val value: Boolean) : AddEditEvent()
    data class Agent(val value: String) : AddEditEvent()
    data class NotSave(val value: Boolean) : AddEditEvent()
    object AddPhotos : AddEditEvent()
    object OnSave : AddEditEvent()
}
