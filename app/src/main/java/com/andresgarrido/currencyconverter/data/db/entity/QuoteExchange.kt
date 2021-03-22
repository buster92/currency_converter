package com.andresgarrido.currencyconverter.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quote_exchange")
data class QuoteExchange(
        @PrimaryKey val exchange: String,
        @ColumnInfo(name = "value") val value: Double
)