package com.aneesh.chatkara.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ItemDao {

    @Insert
    fun insert(itemEntity : ItemEntites)

    @Delete
    fun delete(itemEntity: ItemEntites)

    @Query("SELECT * FROM items")
    fun getAllItems() : List<ItemEntites>

    @Query("SELECT * FROM items WHERE id = :itemId")
    fun getItembyId(itemId : String) : ItemEntites

    @Query("DELETE FROM items WHERE id = :id")
    fun deleteOrders(id : String)

}