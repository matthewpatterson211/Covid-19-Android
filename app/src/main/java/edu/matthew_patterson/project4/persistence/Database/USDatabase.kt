package edu.matthew_patterson.project4.persistence.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.matthew_patterson.project4.persistence.daos.USDao
import edu.matthew_patterson.project4.persistence.entities.USEntity

@Database(entities = [USEntity::class], version = 2)
abstract class USDatabase: RoomDatabase() {
    abstract fun usDao(): USDao
}