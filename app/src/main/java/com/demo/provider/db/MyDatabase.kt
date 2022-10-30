package com.demo.provider.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Student::class]
, version = 1 , exportSchema = false)
abstract class MyDatabase : RoomDatabase() {
     abstract fun studentDao():StudentDao
    companion object {
        val DB_NAME = "demo.db"

        @Volatile
        private var instance: MyDatabase? = null
        fun getInstance(context: Context): MyDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context)

            }
        }

        fun buildDatabase(context: Context): MyDatabase {
            return Room.databaseBuilder(context, MyDatabase::class.java, DB_NAME).build()
        }
    }
}