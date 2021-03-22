package com.andresgarrido.currencyconverter.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency_type")
data class CurrencyType(
        @PrimaryKey val name: String,
        @ColumnInfo() val description: String
)