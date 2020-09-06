package edu.matthew_patterson.project4

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.matthew_patterson.project4.persistence.SharedPreference
import kotlinx.android.synthetic.main.settings_menu_row.view.*

class SettingsAdapter(private val resultsArray: Array<StateInfo>, context: Context): RecyclerView.Adapter<CustomViewHolder>() {

    private val sharedPreference: SharedPreference =
        SharedPreference(context)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.settings_menu_row, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        return resultsArray.size
    }

        override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
            holder.view.state_abbreviation?.text = resultsArray[position].name
            holder.view.covid_site.text = resultsArray[position].covid19Site
            holder.view.setOnClickListener {
                sharedPreference.save("selected state", resultsArray[position].state)
                (holder.view.context as Activity).finish()
            }
            println(resultsArray[position])
        }

}

class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view)