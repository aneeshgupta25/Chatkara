package com.aneesh.chatkara.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ResDao {


    //Res Dao
    @Insert
    fun insert(resEntity : ResEntities)

    @Delete
    fun delete(resEntity : ResEntities)

    @Query("SELECT * FROM restaurants")
    fun getAllRes() : MutableList<ResEntities>

    //colon before resID is important
    @Query("SELECT * FROM restaurants WHERE id = :resID")
    fun getResByID(resID : String) : ResEntities

}