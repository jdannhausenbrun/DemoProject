package com.jdannhausenbrun.demoproject.database.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jdannhausenbrun.demoproject.database.entities.Country
import kotlinx.coroutines.flow.Flow

@Dao
interface CountryDao {
    @Query("SELECT * FROM Country")
    fun getAll(): Flow<List<Country>>

    @Query("SELECT * FROM Country WHERE name LIKE '%' || :query || '%'  ")
    fun getByName(query: String): PagingSource<Int, Country>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(countries: List<Country>)
}