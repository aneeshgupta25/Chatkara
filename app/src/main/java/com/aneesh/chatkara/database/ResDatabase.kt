package com.aneesh.chatkara.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ResEntities::class, ItemEntites::class], version = 1, exportSchema = false)
abstract class ResDatabase : RoomDatabase() {

    abstract fun resDao() : ResDao

    abstract fun itemDao() : ItemDao
}