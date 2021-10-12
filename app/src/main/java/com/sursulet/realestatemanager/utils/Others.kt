package com.sursulet.realestatemanager.utils

import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.sursulet.realestatemanager.data.local.model.Address
import java.text.DecimalFormat
import java.time.LocalDate

object Others {

    fun formattedPrice(price: Double): String {
        val df = DecimalFormat("#.##")
        return df.format(price)
    }

    fun formattedAddress(address: Address): String = address.let {
        it.street + if (it.extras.isNotEmpty()) ", ${it.extras}," else ", " + "${it.postCode} ${it.city}" + if (it.state.isNotEmpty()) ", ${it.extras}," else ", " + it.country
    }

    fun round(number: Double) = "%.4f".format(number).toDouble() //"%.3f".format(number).toDouble()

    fun getRadius(zoomLevel: Float): Double {
        var meters = 0.0
        when (zoomLevel) {
            0f -> meters = 40075017.0
            1f -> meters = 20037508.0
            2f -> meters = 10018754.0
            3f -> meters = 5009377.1
            4f -> meters = 2504688.5
            5f -> meters = 1252344.3
            6f -> meters = 626172.1
            7f -> meters = 313086.1
            8f -> meters = 156543.0
            9f -> meters = 78271.5
            10f -> meters = 39135.8
            11f -> meters = 19567.9
            12f -> meters = 9783.94
            13f -> meters = 4891.97
            14f -> meters = 2445.98
            15f -> meters = 1222.99
            16f -> meters = 611.496
            17f -> meters = 305.748
            18f -> meters = 152.874
            19f -> meters = 76.437
            20f -> meters = 38.2185
            21f -> meters = 19.10926
            22f -> meters = 9.55463
        }

        return meters
    }

    fun View.showSnackBar(
        view: View,
        msg: String,
        length: Int,
        actionMessage: CharSequence?,
        action: (View) -> Unit
    ) {
        val snackBar = Snackbar.make(view, msg, length)
        if (actionMessage != null) {
            snackBar.setAction(actionMessage) { action(this) }.show()
        } else {
            snackBar.show()
        }
    }

    fun calculatePeriod(number: Long, time: String): LocalDate {
        lateinit var period: LocalDate
        val date = LocalDate.now()

        when (time) {
            "days" -> period = date.minusDays(number)
            "weeks" -> period = date.minusWeeks(number)
            "month" -> period = date.minusMonths(number)
            "years" -> period = date.minusYears(number)
        }

        return period
    }
}