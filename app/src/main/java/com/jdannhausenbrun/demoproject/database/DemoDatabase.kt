package com.jdannhausenbrun.demoproject.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jdannhausenbrun.demoproject.database.daos.CountryDao
import com.jdannhausenbrun.demoproject.database.entities.Country

@Database(entities = [Country::class], version = 1)
abstract class DemoDatabase : RoomDatabase() {
    abstract fun countryDao(): CountryDao

    companion object {
        private const val NAME = "demo_database"
        private var instance: DemoDatabase? = null

        @Synchronized
        fun getDb(context: Context): DemoDatabase {
            if (instance != null) {
                return instance!!
            }

            instance = Room.databaseBuilder(
                context.applicationContext,
                DemoDatabase::class.java,
                NAME
            ).build()

            return instance!!
        }
    }
}