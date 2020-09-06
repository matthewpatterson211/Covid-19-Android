package edu.matthew_patterson.project4

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import edu.matthew_patterson.project4.persistence.StateRepository
import edu.matthew_patterson.project4.persistence.USRepository
import edu.matthew_patterson.project4.persistence.entities.StateEntity
import edu.matthew_patterson.project4.persistence.entities.USEntity
import kotlinx.coroutines.runBlocking
import okhttp3.internal.http2.Http2Connection

class Model(application: Application): AndroidViewModel(application) {


    private val stateRepository = StateRepository(application)
    private val usRepository = USRepository(application)

    var listener: Http2Connection.Listener? = null
    interface Listener {
        fun finished()
    }


    var selectedState: String = "MO"


    fun saveStates() {
        stateRepository.saveStates(result)
    }

    fun saveUS(fetchedResults: List<USEntity>) {
        usRepository.saveUS(fetchedResults)
    }

    fun fetchUS(): List<USEntity> {
        return usRepository.fetchUS()
    }

    var result: List<StateEntity>? = null

    suspend fun fetchResults(): List<StateEntity> {

        return stateRepository.fetchStates()
    }

    fun checkIfStateRoomExists(): Boolean = runBlocking {
         stateRepository.getRowCount() > 0
    }


}
