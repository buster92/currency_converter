package com.andresgarrido.currencyconverter.ui.main

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.andresgarrido.currencyconverter.data.CurrencyListRepository
import com.andresgarrido.currencyconverter.data.QuoteExchangeRepository
import com.andresgarrido.currencyconverter.data.network.Resource
import com.andresgarrido.currencyconverter.model.QuoteItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception


class OverviewViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val currencyListRepository = CurrencyListRepository(application)
    private val quoteExchangeRepository = QuoteExchangeRepository(application)

    //external
    val errorMessage: MutableLiveData<String> = MutableLiveData("")

    val currencyListMediator = MediatorLiveData<List<String>>()

    val selectedCurrencyData: MutableLiveData<String> = MutableLiveData<String>("")

    val quoteListMediator = MediatorLiveData<List<QuoteItem>>()

    val progressVisible = MutableLiveData(View.GONE)

    //internal
    private var enteredAmount: MutableLiveData<Double> = MutableLiveData(0.0)

    private val quoteList: LiveData<List<QuoteItem>> = quoteExchangeRepository
        .getQuoteExchangeFor(selectedCurrencyData.value ?: "").map { list ->
            val result = ArrayList<QuoteItem>()
            if (list.data != null) {
                list.data.map { item ->
                    result.add(QuoteItem(item.exchange, item.value))
                }
            }
            result
        }

    private val currencyList: LiveData<List<String>> =
        currencyListRepository.getAllCurrencyTypes().map { list ->
            val result = ArrayList<String>()
            if (list.data != null) {
                list.data.map { item ->
                    result.add(item.name)
                }
            }
            result
        }


    fun onPasswordTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        Log.d("BIND", "text changed: $s")

        var amount = 0.0
        if (s.isEmpty()) {
            errorMessage.value = ""
            return
        }

        try {
            amount = s.toString().toDouble()

            errorMessage.value = ""
        } catch (e: Exception) {
            e.printStackTrace()
            errorMessage.value = "Unable to convert amount."
        }
        generateConversionRates(amount)
    }

    private fun loadQuotes() {


    }

    private fun generateConversionRates(amount: Double) {

        val currency = selectedCurrencyData.value
        Log.d("ViewModel", "currency: $currency")

        enteredAmount.postValue(amount)
    }

    init {
        getData()
    }

    private fun getData() {
        currencyListMediator.addSource(currencyList) {
            currencyListMediator.value = it
        }

        quoteListMediator.addSource(quoteList) {
            quoteListMediator.value = it
        }
        quoteListMediator.addSource(selectedCurrencyData) {
            errorMessage.postValue("")
            Log.d("VM", "quoteListMediator, currency selected: $it")
            if (it.isNullOrEmpty())
                return@addSource

            progressVisible.postValue(View.VISIBLE)
            viewModelScope.launch(Dispatchers.IO) {
                val response = quoteExchangeRepository.getQuoteExchangeList(it)
                if (response.status == Resource.Status.ERROR) {
                    errorMessage.postValue(response.message!!)
                    quoteListMediator.postValue(ArrayList())
                } else {
                    quoteListMediator.postValue(
                        response.data?.map { item ->
                            QuoteItem(item.exchange, item.value)
                        }
                    )
                }
                progressVisible.postValue(View.GONE)
            }
        }
        quoteListMediator.addSource(enteredAmount) {
            errorMessage.postValue("")
            val currency = selectedCurrencyData.value
            if (currency.isNullOrEmpty())
                return@addSource

            viewModelScope.launch(Dispatchers.IO) {
                val response = quoteExchangeRepository.getQuoteExchangeList(currency)
                if (response.status == Resource.Status.ERROR) {
                    errorMessage.postValue(response.message!!)
                    quoteListMediator.postValue(ArrayList())
                } else {
                    quoteListMediator.postValue(
                        response.data?.map { item ->
                            QuoteItem(item.exchange, item.value * it)
                        }
                    )

                }
            }
        }
    }
}
