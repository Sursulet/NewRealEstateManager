package com.sursulet.realestatemanager.utils

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@SuppressLint("MissingPermission")
@ExperimentalCoroutinesApi
suspend fun FusedLocationProviderClient.awaitLastLocation(): Location =
    suspendCancellableCoroutine { continuation ->
        lastLocation.addOnSuccessListener { location ->
            continuation.resume(location)
        }.addOnFailureListener { e ->
            continuation.resumeWithException(e)
        }
    }

@SuppressLint("MissingPermission")
@ExperimentalCoroutinesApi
fun FusedLocationProviderClient.locationFlow() = callbackFlow<Location> {

    val callback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult?) {
            result ?: return
            for (location in result.locations) {
                try {
                    trySend(location)
                } catch (t: Throwable) {
                    Log.d("LOCATION", "onLocationResult: ",t)
                }
            }
        }
    }

    requestLocationUpdates(
        createLocationRequest(),
        callback,
        Looper.getMainLooper()
    ).addOnFailureListener { e -> close(e) }

    awaitClose { removeLocationUpdates(callback) }
}

fun createLocationRequest(): LocationRequest = LocationRequest.create().apply {
    interval = 3000
    fastestInterval = 2000
    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
}