/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.andresgarrido.currencyconverter.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andresgarrido.currencyconverter.network.MarsApiService
import kotlinx.coroutines.launch

/**
 * The [ViewModel] that is attached to the [OverviewFragment].
 */
class OverviewViewModel : ViewModel() {

    enum class LoadingStatus { LOADING, DONE, ERROR }

    private val LOG_TAG = "CONVERTER_VM"

    // internals
    private val _response = MutableLiveData<String>()
    private val _status = MutableLiveData<LoadingStatus>()

    // externals
    val response: LiveData<String>
        get() = _response
    val status: LiveData<LoadingStatus>
        get() = _status


    init {
        getData()
    }

    private fun getData() {
        viewModelScope.launch {
            val currencyList = MarsApiService.retrofitService.getProperties()
            if (currencyList.success) {

                if (currencyList.currencies == null) {
                    _response.value = "Currencies data broken"
                    return@launch
                }

                _response.value = "Data fetched successfully"

                for ((k, v) in currencyList.currencies) {
                    Log.d(LOG_TAG, "$k: $v")
                }
            }
            else {
                val errorMessage = currencyList.error?.info
                _response.value = "Error: $errorMessage"
            }
        }
    }
}
