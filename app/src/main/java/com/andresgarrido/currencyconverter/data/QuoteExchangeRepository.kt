package com.andresgarrido.currencyconverter.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.andresgarrido.currencyconverter.CurrencyPreferences
import com.andresgarrido.currencyconverter.data.db.AppDatabase
import com.andresgarrido.currencyconverter.data.db.dao.QuoteExchangeDao
import com.andresgarrido.currencyconverter.data.db.entity.QuoteExchange
import com.andresgarrido.currencyconverter.model.CurrencyQuotesResponse
import com.andresgarrido.currencyconverter.data.network.ApiRemoteDataSource
import com.andresgarrido.currencyconverter.data.network.Resource
import kotlinx.coroutines.Dispatchers

class QuoteExchangeRepository(private val context: Application) {

    private val quoteExchangeDao: QuoteExchangeDao = AppDatabase.getDatabase(context).quoteExchangeDao()
    private val apiRemoteDataSource = ApiRemoteDataSource()
    private val preferences = CurrencyPreferences.getInstance(context)

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    suspend fun getQuoteExchangeList(currencyCode: String): Resource<List<QuoteExchange>> {

        if (canRequestNetworkData(currencyCode)) {
            val remoteData = apiRemoteDataSource.getExchangeRates(currencyCode)
            if (remoteData.status == Resource.Status.SUCCESS && remoteData.data != null) {
                responseToDtoAndInsert(remoteData.data)
                //set new updated time
                preferences.setLastUpdatedCurrencyQuote(currencyCode)
            }
            else if (remoteData.status == Resource.Status.ERROR) {
                return Resource.error("An error has occurred.")
            }

        }
        return Resource.success(quoteExchangeDao.findAllByCurrency(currencyCode))
    }



    fun getQuoteExchangeFor(currencyCode: String) = performGetOperation(
        databaseQuery = { quoteExchangeDao.findAllByCurrencyLive(currencyCode) },
        networkCall = { apiRemoteDataSource.getExchangeRates(currencyCode) },
        saveCallResult = { responseToDtoAndInsert(it) },
        currencyCode = currencyCode
    )

    private fun responseToDtoAndInsert(response: CurrencyQuotesResponse): List<QuoteExchange> {
        val toInsert = response.quotes?.map { quoteMap ->
            QuoteExchange(quoteMap.key, quoteMap.value)
        }
        toInsert?.let {
            quoteExchangeDao.insertAll(it)
            return it
        }
        return ArrayList()
    }

    private fun <T, A> performGetOperation(
        databaseQuery: () -> LiveData<T>,
        networkCall: suspend () -> Resource<A>,
        saveCallResult: suspend (A) -> Unit,
        currencyCode: String
    ): LiveData<Resource<T>> =
        liveData(Dispatchers.IO) {
            Log.d("REPO", "===QuoteExchangeRepository performGetOperation===")
            emit(Resource.loading())
            //gets local data
            val source = databaseQuery.invoke().map { Resource.success(it) }
            emitSource(source)

            if (canRequestNetworkData(currencyCode)) {
                val responseStatus = networkCall.invoke()
                if (responseStatus.status == Resource.Status.SUCCESS) {
                    Log.d("REPO", "Network request Ok")
                    //if ok saves the result in db
                    saveCallResult(responseStatus.data!!)

                } else if (responseStatus.status == Resource.Status.ERROR) {
                    Log.e("REPO", "Network request Error")
                    emit(Resource.error(responseStatus.message!!))
                    emitSource(source)
                }
                //set new updated time
                preferences.setLastUpdatedCurrencyQuote(currencyCode)
            }
        }

    private fun canRequestNetworkData(currencyCode: String): Boolean {
        val timePassFromUpdateMinutes = preferences.getLastUpdatedCurrencyQuote(currencyCode) / 60 / 1000
        //can get data if 30 minutes has passed since last update

        Log.d("REPO", "time passed since update $timePassFromUpdateMinutes minutes")
        return timePassFromUpdateMinutes > 30
    }
}