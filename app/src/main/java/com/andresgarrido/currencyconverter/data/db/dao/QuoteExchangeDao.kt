package com.andresgarrido.currencyconverter.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.andresgarrido.currencyconverter.data.db.entity.QuoteExchange

@Dao
interface QuoteExchangeDao {

    @Query("SELECT * FROM quote_exchange WHERE exchange LIKE :currencyCode || '%'")
    fun findAllByCurrencyLive(currencyCode: String): LiveData<List<QuoteExchange>>


    @Query("SELECT * FROM quote_exchange WHERE exchange LIKE :currencyCode || '%'")
    fun findAllByCurrency(currencyCode: String): List<QuoteExchange>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(quoteExchanges: List<QuoteExchange>)

    @Delete
    fun delete(quoteExchange: QuoteExchange)
}