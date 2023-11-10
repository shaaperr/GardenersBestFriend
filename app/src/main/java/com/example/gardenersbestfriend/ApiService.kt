package com.example.gardenersbestfriend

import com.example.gardenersbestfriend.DataModel
import retrofit2.http.GET
import  retrofit2.http.Path

interface ApiService {
    @GET("plant type/{plantType}")
    suspend fun getPlantByName(@Path("plantType") plantType: string): DataModel
}