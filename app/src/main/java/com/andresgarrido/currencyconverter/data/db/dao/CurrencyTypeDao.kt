package com.andresgarrido.currencyconverter.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.andresgarrido.currencyconverter.data.db.entity.CurrencyType

@Dao
interface CurrencyTypeDao {

    @Query("SELECT * FROM currency_type")
    fun findAll(): LiveData<List<CurrencyType>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(currencyType: List<CurrencyType>)

    @Delete
    fun delete(currencyType: CurrencyType)
}