import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlantData(
    val data: List<PlantItem>
)

@Serializable
data class PlantItem(
    val id: Int,
    @SerialName("scientific_name")
    val scientific_name: List<String>,
    val watering: String,
    val sunlight: Any,
    val cycle: String
)

