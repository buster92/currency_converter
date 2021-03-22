package com.andresgarrido.currencyconverter

import android.content.Context
import android.content.SharedPreferences

class CurrencyPreferences(private val context: Context) {
    private val PREFS_NAME = "currency.converted.prefs.name"
    private val PREF_KEY_CURRENCY_UPDATED = "key.last.updated.currency.quote"
    private val PREF_KEY_CURRENCY_TYPE_UPDATED = "key.last.updated.currency.type"

    companion object {
        @Volatile
        private var INSTANCE: CurrencyPreferences? = null

        fun getInstance(context: Context): CurrencyPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = CurrencyPreferences(context)
                INSTANCE = instance
                instance
            }
        }
    }

    private fun getSharedPrefs(): SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)


    fun getLastUpdatedCurrencyQuote(currencyCode: String): Long {
        val now = System.currentTimeMillis()
        val key = PREF_KEY_CURRENCY_UPDATED + currencyCode
        val lastUpdatedTime: Long = getSharedPrefs().getLong(key, 0)
        return now - lastUpdatedTime
    }

    fun setLastUpdatedCurrencyQuote(currencyCode: String) =
        getSharedPrefs().edit().putLong(
            PREF_KEY_CURRENCY_UPDATED + currencyCode,
            System.currentTimeMillis()
        ).apply()


    fun getLastUpdatedCurrencyList(): Long {
        val now = System.currentTimeMillis()
        val lastUpdatedTime: Long = getSharedPrefs().getLong(PREF_KEY_CURRENCY_TYPE_UPDATED, 0)
        return now - lastUpdatedTime
    }

    fun setLastUpdatedCurrencyType() =
        getSharedPrefs().edit().putLong(
            PREF_KEY_CURRENCY_TYPE_UPDATED,
            System.currentTimeMillis()
        ).apply()

}