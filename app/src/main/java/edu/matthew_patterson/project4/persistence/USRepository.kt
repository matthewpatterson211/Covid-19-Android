package edu.matthew_patterson.project4.persistence

import android.app.Activity
import android.content.Context
import androidx.room.Room
import edu.matthew_patterson.project4.persistence.Database.USDatabase
import edu.matthew_patterson.project4.persistence.entities.USEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class USRepository (private var ctx: Context) {
    private val db: USDatabase



    init {
        if (ctx is Activity) {
            ctx = ctx.applicationContext
        }
        db = Room.databaseBuilder(ctx, USDatabase::class.java, "us.sqlite")

            .build()

    }

    fun saveUS(us: List<USEntity>?) {

        CoroutineScope(Dispatchers.Default).launch {
            val entities = us?.map {
                USEntity(it.hash, it.dateChecked, it.death, it.deathIncrease)
            }
            if (entities != null) {
                db.usDao().insertUS(entities.toTypedArray())
            }
        }

    }



    fun fetchUS(): List<USEntity> {
        return runBlocking {
            return@runBlocking db.usDao().fetchUS()
        }



    }


}