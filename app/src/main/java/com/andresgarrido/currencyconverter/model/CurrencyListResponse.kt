package com.andresgarrido.currencyconverter.model

data class CurrencyListResponse(
    val success: Boolean,
    val currencies: Map<String, String>? = null,
    val error: ErrorResponse? = null
)