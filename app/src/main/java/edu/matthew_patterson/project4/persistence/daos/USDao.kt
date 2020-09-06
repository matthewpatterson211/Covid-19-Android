package edu.matthew_patterson.project4.persistence.daos

import androidx.room.*
import edu.matthew_patterson.project4.persistence.entities.USEntity

@Dao
interface USDao {

    @Transaction

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUS(us: Array<USEntity>)

    @Query("SELECT * FROM us ORDER BY dateChecked DESC")
    suspend fun fetchUS(): List<USEntity>


}
