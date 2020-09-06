package edu.matthew_patterson.project4.persistence.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.matthew_patterson.project4.persistence.daos.StateDao
import edu.matthew_patterson.project4.persistence.entities.StateEntity

@Database(entities = [StateEntity::class], version = 2)
abstract class StatesDatabase: RoomDatabase() {
    abstract fun statesDao(): StateDao
}