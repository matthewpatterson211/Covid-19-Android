package edu.matthew_patterson.project4

import org.json.JSONObject

data class State (
    var state: String = "",
    var dateChecked: String = "",
    var death: Int = 0,
    var deathIncrease: Int = 0
//    var hospitalized: Int = 0,
//    var hospitalizedIncrease: Int = 0,
//    var negative: Int = 0,
//    var negativeIncrease: Int = 0,
//    var positive: Int = 0,
//    var positiveIncrease: Int = 0,
//    var totalTestResults: Int = 0,
//    var totalTestResultsIncrease: Int = 0

    )

{
    constructor(stateObject: JSONObject): this(
        state = stateObject.getString("state"),
        dateChecked = stateObject.getString("dateChecked"),
        death = stateObject.getInt("death"),
        deathIncrease = stateObject.getInt("deathIncrease")

    )
}

