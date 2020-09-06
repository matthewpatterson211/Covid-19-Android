package edu.matthew_patterson.project4


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.settings_activity.*
import okhttp3.*
import java.io.IOException

class SettingsActivity: AppCompatActivity() {

    private lateinit var model: Model

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)



        model = ModelHolder.instance.get(Model::class) ?: Model(application)

        fetchJson()

        setting_RecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun fetchJson() {
        println("Attempting to Fetch JSON")

        val url = "https://covidtracking.com/api/v1/states/info.json"

        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                println(body)

                val gson = GsonBuilder().create()

                val resultsArray: Array<StateInfo> = gson.fromJson(body, Array<StateInfo>::class.java)

                runOnUiThread {
                    setting_RecyclerView.adapter = SettingsAdapter(resultsArray, applicationContext)
                }

            }

        })
    }
}