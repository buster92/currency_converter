package com.andresgarrido.currencyconverter.network

import com.andresgarrido.currencyconverter.model.CurrencyListResponse
import retrofit2.http.GET

interface ApiInterface {

    @GET("list")
    suspend fun getProperties(): CurrencyListResponse
}