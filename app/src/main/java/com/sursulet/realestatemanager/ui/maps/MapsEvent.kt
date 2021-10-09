package com.sursulet.realestatemanager.ui.maps

sealed class MapsEvent {
    object MapReady : MapsEvent()
    object LastKnownLocation : MapsEvent()
    object StartUpdatingLocation : MapsEvent()
    data class Zoom(val value: Float) : MapsEvent()
}
