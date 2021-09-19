package com.jdannhausenbrun.demoproject.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Country(
    @PrimaryKey val code: String,
    @ColumnInfo(name = "name") val name: String
)