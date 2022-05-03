package com.aneesh.chatkara.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//annotation tells the compiler that what are we making

@Entity(tableName = "restaurants")
data class ResEntities (
    @PrimaryKey val id : String,
    @ColumnInfo(name = "res_name") val name : String,
    @ColumnInfo(name = "res_rating") val rating : String,
    @ColumnInfo(name = "res_cost")val cost_for_one : String,
    @ColumnInfo(name = "res_image_url") val image_url : String
)