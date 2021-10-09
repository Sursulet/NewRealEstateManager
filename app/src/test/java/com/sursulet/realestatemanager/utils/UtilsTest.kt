package com.sursulet.realestatemanager.utils

import com.google.common.truth.Truth
import org.junit.Test
import java.time.LocalDate
import java.util.*

class UtilsTest {

    @Test
    fun `when convert 5 dollars to euros return 4`() {
        val result = Utils.convertDollarToEuro(5)
        Truth.assertThat(result).isEqualTo(4)
    }

    @Test
    fun `when convert 10 euros to dollars return 4`() {
        val result = Utils.convertEuroToDollar(10)
        Truth.assertThat(result).isEqualTo(12)
    }

    @Test
    fun `convert dollars to euros`() {
        val result = Utils.convertDollarToEuro(7)
        val test = Utils.convertEuroToDollar(result)
        Truth.assertThat(result).isEqualTo(6)
        Truth.assertThat(test).isEqualTo(7)
    }

    @Test
    fun `convert today's date `() {
        val result = Utils.getTodayDate().split("/")
        val excepted = LocalDate.now().toString().split("-")
        Truth.assertThat(result).isEqualTo(excepted.asReversed())
    }

    @Test
    fun `returns date `() {
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH] + 1
        val day = calendar[Calendar.DAY_OF_MONTH]

        val date = "${String.format("%02d",day)}/${String.format("%02d",month)}/$year"
        val result = Utils.getTodayDate()
        Truth.assertThat(result).isEqualTo(date)
    }
}