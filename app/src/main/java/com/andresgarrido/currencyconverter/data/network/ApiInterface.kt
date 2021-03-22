package com.andresgarrido.currencyconverter.data.network

import com.andresgarrido.currencyconverter.model.CurrencyListResponse
import com.andresgarrido.currencyconverter.model.CurrencyQuotesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("list")
    suspend fun getCurrencyList(): Response<CurrencyListResponse>

    @GET("live")
    suspend fun getExchangeRates(@Query("source") source: String): Response<CurrencyQuotesResponse>
}