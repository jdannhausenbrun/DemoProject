package com.jdannhausenbrun.demoproject.utils

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import com.jdannhausenbrun.demoproject.database.DemoDatabase
import com.jdannhausenbrun.demoproject.database.entities.Country

@Database(entities = [Country::class], version = 1)
abstract class TestDB: DemoDatabase() {
    companion object {
        fun createTestDB(context: Context): TestDB {
            return Room.inMemoryDatabaseBuilder(context, TestDB::class.java)
                .allowMainThreadQueries()
                .setTransactionExecutor {
                    it.run()
                }
                .build()
        }
    }
}