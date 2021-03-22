package com.andresgarrido.currencyconverter.model

class CurrencyQuotesResponse(
    val success: Boolean,
    val quotes: Map<String, Double>? = null,
    val error: ErrorResponse? = null
)