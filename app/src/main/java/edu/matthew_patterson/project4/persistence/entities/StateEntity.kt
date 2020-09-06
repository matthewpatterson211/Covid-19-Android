package edu.matthew_patterson.project4.persistence.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "states")
data class StateEntity(
    @PrimaryKey var hash: String,
    @ColumnInfo var state: String,
    @ColumnInfo var dateChecked: String,
    @ColumnInfo var death: Int,
    @ColumnInfo var deathIncrease: Int
)