package com.aneesh.chatkara.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class ItemEntites (
    @PrimaryKey val id : String,
    @ColumnInfo(name = "food_items") val foodItems : String
)