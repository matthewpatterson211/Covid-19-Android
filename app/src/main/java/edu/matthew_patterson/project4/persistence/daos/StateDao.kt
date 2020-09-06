package edu.matthew_patterson.project4.persistence.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import edu.matthew_patterson.project4.persistence.entities.StateEntity

@Dao
interface StateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStates(states: Array<StateEntity>)

    @Query("SELECT * FROM states ORDER BY dateChecked DESC")
    suspend fun fetchStates(): List<StateEntity>

    @Query("Select COUNT(hash) FROM states")
     fun getRowCount(): Int
}

