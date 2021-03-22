package com.andresgarrido.currencyconverter.data

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.andresgarrido.currencyconverter.CurrencyPreferences
import com.andresgarrido.currencyconverter.data.db.AppDatabase
import com.andresgarrido.currencyconverter.data.db.entity.CurrencyType
import com.andresgarrido.currencyconverter.data.network.ApiRemoteDataSource
import com.andresgarrido.currencyconverter.data.network.Resource
import com.andresgarrido.currencyconverter.model.CurrencyListResponse
import kotlinx.coroutines.Dispatchers

class CurrencyListRepository(private val context: Application) {

    private val currencyTypeDao = AppDatabase.getDatabase(context).currencyTypeDao()
    private val apiRemoteDataSource = ApiRemoteDataSource()

    fun getAllCurrencyTypes() = performGetOperation(
        databaseQuery = { currencyTypeDao.findAll() },
        networkCall = { apiRemoteDataSource.getCurrencyList() },
        saveCallResult = { transformResponseToDto(it) },
        CurrencyPreferences.getInstance(context)
    )

    private fun transformResponseToDto(response: CurrencyListResponse): List<CurrencyType> {
        val toInsert = response.currencies?.map { quoteMap ->
            CurrencyType(quoteMap.key, quoteMap.value)
        }
        toInsert?.let {
            currencyTypeDao.insertAll(it)
            return it
        }
        return ArrayList()
    }

    private fun <T, A> performGetOperation(
        databaseQuery: () -> LiveData<T>,
        networkCall: suspend () -> Resource<A>,
        saveCallResult: suspend (A) -> Unit,
        preferences: CurrencyPreferences
    ): LiveData<Resource<T>> =
        liveData(Dispatchers.IO) {
            Log.d("REPO", "===CurrencyListRepository performGetOperation===")
            emit(Resource.loading())
            //gets local data
            val source = databaseQuery.invoke().map { Resource.success(it) }
            emitSource(source)

            val timePassFromUpdateHour= preferences.getLastUpdatedCurrencyList() / 1000 / 60 / 60
            //if 1 day has passed since last update, get data again

            Log.d("REPO", "time passed since update $timePassFromUpdateHour hours")
            if (timePassFromUpdateHour > 24) {
                val responseStatus = networkCall.invoke()
                if (responseStatus.status == Resource.Status.SUCCESS) {
                    Log.d("REPO", "Network request Ok")
                    //if ok saves the result in db
                    saveCallResult(responseStatus.data!!)

                } else if (responseStatus.status == Resource.Status.ERROR) {
                    Log.d("REPO", "Network request Error")
                    emit(Resource.error(responseStatus.message!!))
                    emitSource(source)
                }
                //set new updated time
                preferences.setLastUpdatedCurrencyType()
            }
        }
}