import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/api/species-list")
    fun getPlantData(@Query("key") apiKey: String): Call<PlantData>

    @GET("/api/species-list")
    fun getPlantDataByName(@Query("key") apiKey: String, @Query("q") plantName: String): Call<PlantData>
}