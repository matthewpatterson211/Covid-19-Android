package edu.matthew_patterson.project4.persistence

import android.app.Activity
import android.content.Context
import androidx.room.Room
import edu.matthew_patterson.project4.persistence.Database.StatesDatabase
import edu.matthew_patterson.project4.persistence.entities.StateEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class StateRepository (private var ctx: Context) {
    private val db: StatesDatabase



    init {
        if (ctx is Activity) {
            ctx = ctx.applicationContext
        }
        db = Room.databaseBuilder(ctx, StatesDatabase::class.java, "states.sqlite")
            .allowMainThreadQueries()
            .build()
    }

    fun saveStates(states: List<StateEntity>?) {

        CoroutineScope(Dispatchers.Default).launch {
            val entities = states?.map {
                StateEntity(it.hash, it.state, it.dateChecked, it.death, it.deathIncrease)
            }
            if (entities != null) {
                db.statesDao().insertStates(entities.toTypedArray())
            }

        }

    }



    fun fetchStates(): List<StateEntity> {
        return runBlocking {
            return@runBlocking db.statesDao().fetchStates()
        }

    }

    fun getRowCount(): Int {

        return db.statesDao().getRowCount()
    }




}