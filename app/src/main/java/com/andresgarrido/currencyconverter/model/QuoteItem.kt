package com.andresgarrido.currencyconverter.model

import java.text.DecimalFormat
import java.text.NumberFormat

class QuoteItem(val currency: String, private val rate: Double) {
    open fun getRateString(): String {
        val formatter: NumberFormat = DecimalFormat("###,###.##")

        return formatter.format(rate)
    }

    open fun getRate(): Double {
        return rate
    }
}