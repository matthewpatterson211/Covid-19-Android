package edu.matthew_patterson.project4.persistence.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "us")
data class USEntity(
    @PrimaryKey var hash: String,
    @ColumnInfo var dateChecked: String,
    @ColumnInfo var death: Int,
    @ColumnInfo var deathIncrease: Int
)