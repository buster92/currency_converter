package com.andresgarrido.currencyconverter.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.andresgarrido.currencyconverter.data.db.dao.CurrencyTypeDao
import com.andresgarrido.currencyconverter.data.db.dao.QuoteExchangeDao
import com.andresgarrido.currencyconverter.data.db.entity.CurrencyType
import com.andresgarrido.currencyconverter.data.db.entity.QuoteExchange

@Database(entities = [QuoteExchange::class, CurrencyType::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun quoteExchangeDao(): QuoteExchangeDao
    abstract fun currencyTypeDao(): CurrencyTypeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "converter_app_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }

    }
}