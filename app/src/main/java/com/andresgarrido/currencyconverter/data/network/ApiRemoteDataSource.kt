package com.andresgarrido.currencyconverter.data.network

import android.util.Log
import com.andresgarrido.currencyconverter.model.CurrencyListResponse
import com.andresgarrido.currencyconverter.model.CurrencyQuotesResponse

class ApiRemoteDataSource: BaseDataSource() {

    private val apiService = ApiService.retrofitService
    suspend fun getExchangeRates(currencyCode: String): Resource<CurrencyQuotesResponse> {

        val result = getResult {
            apiService.getExchangeRates(currencyCode)
        }
        if (result.status == Resource.Status.SUCCESS ) {
            val response = result.data!!
            if (response.error != null) {
                Log.e(
                    ApiRemoteDataSource::class.simpleName,
                    "Error on getExchangeRates(): ${response.error.info}"
                )
                return Resource.error(response.error.info, result.data)
            }
        }
        return result
    }

    suspend fun getCurrencyList(): Resource<CurrencyListResponse> {
        val result = getResult {
            apiService.getCurrencyList()
        }
        if (result.status == Resource.Status.SUCCESS ) {
            val response = result.data!!
            if (response.error != null) {
                Log.e(
                    ApiRemoteDataSource::class.simpleName,
                    "Error on getCurrencyList(): ${response.error.info}"
                )
                return Resource.error(response.error.info, result.data)
            }
        }
        return result
    }

}