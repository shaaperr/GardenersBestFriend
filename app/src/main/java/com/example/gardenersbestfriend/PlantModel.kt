package com.example.gardenersbestfriend

import java.io.Serializable
import java.util.Random

data class PlantModel (
    var id: Int = getAutoId(),
    var name: String = "",
    var date: String = "",
    var reminders: String = "",
    var entry: String = "",
    var imagepath: String = ""

) : Serializable {
    companion object{
        fun getAutoId():Int{
            val random = Random()
            return random.nextInt(100)
        }
    }

}

