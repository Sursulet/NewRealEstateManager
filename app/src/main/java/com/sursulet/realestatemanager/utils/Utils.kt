package com.sursulet.realestatemanager.utils

import android.content.Context
import android.net.ConnectivityManager
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

object Utils {

    /**
     * Converting the price of a property (Dollars to Euros)
     * @param dollars
     * @return
     */
    fun convertDollarToEuro(dollars: Int): Int {
        return (dollars * 0.84).roundToInt()
    }

    /**
     * Converting the price of a property (Euros to Dollars)
     * @param euros
     * @return
     */
    fun convertEuroToDollar(euros: Int): Int {
        return (euros * 1.18).roundToInt()
    }

    /**
     * Converting today's date to a more suitable format
     * @return
     */
    fun getTodayDate(): String {
        val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return LocalDate.now().format(dateFormat)
    }

    /**
     * Checking the network connection
     * @param context
     * @return
     */
    fun isInternetAvailable(context: Context): Boolean {
        val manager = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = manager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
    }
}