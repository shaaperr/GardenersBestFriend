package com.example.gardenersbestfriend

import java.util.Random

data class PlantModel (
    var id: Int = getAutoId(),
    var name: String = "",
    var date: String = "",
    var reminders: String = "",
    var entry: String = ""

){
    companion object{
        fun getAutoId():Int{
            val random = Random()
            return random.nextInt(100)
        }
    }

}

