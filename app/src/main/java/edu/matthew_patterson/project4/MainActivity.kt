package edu.matthew_patterson.project4

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.gson.GsonBuilder
import edu.matthew_patterson.project4.persistence.SharedPreference
import edu.matthew_patterson.project4.persistence.entities.StateEntity
import edu.matthew_patterson.project4.persistence.entities.USEntity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.*
import java.io.IOException


class MainActivity : AppCompatActivity() {





    private lateinit var model: Model

    override fun onDestroy() {
        super.onDestroy()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val sharedPreference =
            SharedPreference(this)

        model = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application))
            .get(Model::class.java)


        model.selectedState = sharedPreference.getValueString("selected state").toString()

        if (model.selectedState == "") {
            model.selectedState = "MO"
        }

        if (model.selectedState == "US") {

        fetchUSJson()
    }
        else {

                if (checkRoom()) {
                    lifecycleScope.launch {
                        loadData(model.fetchResults())
                    }

                }
                else {
                    fetchJson()
                }
            }

        }

    private fun checkRoom(): Boolean = runBlocking {
        model.checkIfStateRoomExists()
    }




    override fun onResume() {
        super.onResume()
        val sharedPreference =
            SharedPreference(this)



        model = ModelHolder.instance.get(Model::class) ?: Model(application)


        model.selectedState = sharedPreference.getValueString("selected state").toString()

        if (model.selectedState == "") {
            model.selectedState = "MO"
        }

        println("${model.selectedState} selected state")

        val loadingText = getString(R.string.graph_loading_state, model.selectedState)
        total_details.text = loadingText
        graph_details.text = getString(R.string.graph_details)

        if (model.selectedState == "US") {
            fetchUSJson()
        }
        else {
            fetchJson()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_select_state -> {
                this.startActivity(Intent(this, SettingsActivity::class.java))
                return true
            }
            R.id.action_us -> {
                val sharedPreference =
                    SharedPreference(
                        this
                    )
                sharedPreference.save("selected state", "US")
                onResume()
                return true
            }
            R.id.action_refresh -> {
                onResume()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }



        private fun fetchJson() {
        println("Attempting to Fetch JSON")


        val url = "https://covidtracking.com/api/v1/states/daily.json"

        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {



            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                println(body)

                val gson = GsonBuilder().create()

                val userArray: Array<StateEntity> = gson.fromJson(body, Array<StateEntity>::class.java)

                model.result = userArray.toList()
                model.saveStates()


                val fetchedResults = userArray.toList()

                runOnUiThread {

                    loadData(fetchedResults)

                }

            }

        })
    }

    fun loadData(fetchedResults: List<StateEntity>){

                val graphData = filterResultsByState(fetchedResults, model.selectedState)

                var reverseGraph = graphData.reversed()


                while (reverseGraph.first().deathIncrease == 0 && reverseGraph.count() != 1)  {


                    reverseGraph = reverseGraph.subList(1, reverseGraph.size)


                }

                val deathTotal = (graphData[0].death).toString()

                val text = getString(R.string.graph_top_details, deathTotal, model.selectedState)
                total_details.text = text

                val dateFilter = reverseGraph.map { it.dateChecked }


                val entries = reverseGraph.mapIndexed { index, value  ->
                    BarEntry(index.toFloat(), value.deathIncrease.toFloat()) }

                 val lineDataSets = BarDataSet(entries, "BarDataSet")

                buildGraph(lineDataSets, dateFilter)

        }





    private fun fetchUSJson() {
        println("Attempting to Fetch JSON")

        val url = "https://covidtracking.com/api/v1/us/daily.json"

        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {

            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                println(body)

                val gson = GsonBuilder().create()


                val userArray: Array<USEntity> = gson.fromJson(body, Array<USEntity>::class.java)

                model.saveUS(userArray.toList())



                println(userArray)

                runOnUiThread {



                    loadUSData(model.fetchUS())

                }

            }

        })
    }

    fun loadUSData(fetchedResults: List<USEntity>){

        var reverseGraph = fetchedResults.reversed()


        while (reverseGraph.first().deathIncrease == 0 && reverseGraph.count() != 1)  {


            reverseGraph = reverseGraph.subList(1, reverseGraph.size)


        }

        val deathTotal = (fetchedResults[0].death).toString()

        val text = getString(R.string.graph_top_details, deathTotal, model.selectedState)
        total_details.text = text

        val dateFilter = reverseGraph.map { it.dateChecked }


        val entries = reverseGraph.mapIndexed { index, value  ->
            BarEntry(index.toFloat(), value.deathIncrease.toFloat()) }

        val lineDataSets = BarDataSet(entries, "BarDataSet")

        buildGraph(lineDataSets, dateFilter)
    }

    private fun buildGraph(barDataSet: BarDataSet, dateFilter: List<String>) {

        val barChartView = findViewById<BarChart>(R.id.barGraph)
        barDataSet.color = Color.RED
        val data = BarData(barDataSet)

        barChartView.data = data
        barChartView.invalidate()
        barChartView.setFitBars(true)
        barChartView.setDrawBorders(false)
        barChartView.setDrawGridBackground(false)
        barChartView.axisLeft.axisMinimum = 0.0f
        val legend = barChartView.legend
        legend.isEnabled = false
        barChartView.axisLeft.setDrawGridLines(false)
        barChartView.axisRight.setDrawGridLines(false)
        barChartView.xAxis.setDrawGridLines(false)
        barChartView.description.isEnabled = false

        barChartView.invalidate()

        barChartView.setOnChartValueSelectedListener(object: OnChartValueSelectedListener{
            override fun onNothingSelected() {
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                val displayValue = e?.y?.toInt().toString()
                val displayDate = dateFilter[e?.x?.toInt()!!].split("T")[0]

                val detailText = getString(R.string.graph_bottom_details_catenation, displayValue, displayDate)
                graph_details.text = detailText
                Log.e("TAG", "$e")
            }
        })
    }

    private fun filterResultsByState(results: List<StateEntity>, state: String): List<StateEntity> {
        return results.filter { it.state == state }
    }



}