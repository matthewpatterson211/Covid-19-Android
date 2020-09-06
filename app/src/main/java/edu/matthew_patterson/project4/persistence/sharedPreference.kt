package edu.matthew_patterson.project4.persistence

import android.content.Context
import android.content.SharedPreferences

class SharedPreference(context: Context) {
    private val PREFSNAME = "kotlincodes"
    private val sharedPref: SharedPreferences = context.getSharedPreferences(PREFSNAME, Context.MODE_PRIVATE)

    fun save(KEY_NAME: String, value: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()

        editor.putString(KEY_NAME, value)

        editor.apply()
    }

    fun getValueString(KEY_NAME: String): String? {
        return sharedPref.getString(KEY_NAME,"")
    }


}
