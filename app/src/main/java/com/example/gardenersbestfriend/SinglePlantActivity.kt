package com.example.gardenersbestfriend

import ApiService
import PlantData
import PlantItem
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apitest.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException

class SinglePlantActivity : AppCompatActivity() {

    private lateinit var sqLiteHelper: SQLiteHelper
    private var plant: PlantModel? = null

   // private lateinit var dateEntryAdapter: DateEntryAdapter
    private lateinit var dateEntryRecyclerView: RecyclerView
    private var adapter: DateEntryAdapter? = null

    //api stuff
    private val apiKey = "sk-0kBH655442e8498862969"
    private val apiService: ApiService = ApiClient.apiService
    private lateinit var recwaterings: TextView
    private lateinit var recsunlights: TextView
    private lateinit var cycles: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_plant)

        //initializing api textviews
        recwaterings = findViewById(R.id.recwatering)
        recsunlights = findViewById(R.id.recsunlight)
        cycles = findViewById(R.id.cycle)




        val imageButton1: ImageButton =findViewById(R.id.home_button)
        imageButton1.setOnClickListener{
            startActivity(Intent(this,MainActivity::class.java))
        }

        val imageButton2: ImageButton =findViewById(R.id.mydiary_button)
        imageButton2.setOnClickListener{
            startActivity(Intent(this,MyDiaryActivity::class.java))
        }

        val imageButton3: ImageButton =findViewById(R.id.history_button)
        imageButton3.setOnClickListener{
            startActivity(Intent(this,MyHistoryActivity::class.java))
        }

        // Initialize the recyclerView
        dateEntryRecyclerView = findViewById(R.id.recyclerViewDateEntry)
        initRecyclerView()
        sqLiteHelper = SQLiteHelper(this)

        val plantId = intent.getIntExtra("plant_id", -1)





        sqLiteHelper = SQLiteHelper(this)

       // val plantId = intent.getIntExtra("plant_id", -1)

        if (plantId != -1) {
            // Fix: Remove the 'val' keyword to use the existing member variable 'plant'
            plant = sqLiteHelper.getOneById(plantId)

            plant?.let { nonNullPlant ->
                val nameTextView = findViewById<TextView>(R.id.plantName)
            //    val dateTextView = findViewById<TextView>(R.id.date)
                val remindersTextView = findViewById<TextView>(R.id.reminders)
            //    val entryTextView = findViewById<TextView>(R.id.entry)
                val imageView = findViewById<ImageView>(R.id.imageView)
                // Log the image path
                Log.d("Image Path", "Image Path: ${nonNullPlant.imagepath}")

                nameTextView.text = nonNullPlant.name
              //  dateTextView.text = nonNullPlant.reminders
                remindersTextView.text = nonNullPlant.date
              //  entryTextView.text = nonNullPlant.entry

                // Load the image
                if (!nonNullPlant.imagepath.isNullOrEmpty()) {
                    try {
                        val file = File(nonNullPlant.imagepath)
                        if (file.exists()) {
                            val inputStream = FileInputStream(file)
                            val bitmap = BitmapFactory.decodeStream(inputStream)
                            imageView.setImageBitmap(bitmap)
                            inputStream.close()
                        } else {
                            // Handle the case where the file is not found
                            imageView.setImageResource(R.drawable.add_plant) // DefaultImg
                        }
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                        imageView.setImageResource(R.drawable.add_plant) // file is not found
                    }
                } else {
                    imageView.setImageResource(R.drawable.add_plant) // there is no image path
                }

                fetchDataForPlant(nonNullPlant.name)

                getEntries(nonNullPlant.name)
            }
        }

        val pencil = findViewById<ImageView>(R.id.pencil)

        pencil.setOnClickListener {
            plant?.let { nonNullPlant ->
                val intent = Intent(this, updatePlantEntry::class.java)
                intent.putExtra("plant_id", plantId)
                intent.putExtra("plant_name", nonNullPlant.name)
                intent.putExtra("plant_image_path", nonNullPlant.imagepath)
                intent.putExtra("plant_reminders", nonNullPlant.date) //weirdly backwards, but works
                Log.d("IntentData", "Reminders: ${nonNullPlant.date}")
                startActivity(intent)
            }
        }
    }

    private fun fetchDataForPlant(plantName: String) {
        val call: Call<PlantData> = apiService.getPlantDataByName(apiKey, plantName)

        // Execute the call asynchronously
        call.enqueue(object : Callback<PlantData> {
            override fun onResponse(call: Call<PlantData>, response: Response<PlantData>) {
                if (response.isSuccessful) {
                    val plantData: PlantData? = response.body()

                    // Accessing the properties
                    val firstPlantItem: PlantItem? = plantData?.data?.firstOrNull()
                    val sunlight: Any? = firstPlantItem?.sunlight
                    val watering: String? = firstPlantItem?.watering
                    val cycle: String? = firstPlantItem?.cycle

                    // Handle the sunlight field
                    handleSunlightField(sunlight)

                    // Display the fetched data in the TextView
                    recwaterings.text = "Watering Recommendation: $watering"
                    recsunlights.text = "Sunlight Recommendation: $sunlight"
                    cycles.text = "Life Span: $cycle"

                    // Do something with the data
                } else {
                    // Handle error
                    Log.e("MainActivity", "Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<PlantData>, t: Throwable) {
                // Handle failure
                Log.e("MainActivity", "Failure: ${t.message}")
            }
        })
    }

    private fun handleSunlightField(sunlight: Any?) {
        when (sunlight) {
            is String -> {
                // The sunlight field is a single string
                // Handle the string case if needed
            }

            is List<*> -> {
                // The sunlight field is a list
                val sunlightList = sunlight as List<String>
                // Do something with the list
            }

            else -> {
                // Handle other cases if needed
            }
        }
    }

    override fun onResume() {
        super.onResume()

        plant?.let { nonNullPlant ->
            val plantId = intent.getIntExtra("plant_id", -1)

            if (plantId != -1) {
                plant = sqLiteHelper.getOneById(plantId)

                plant?.let { nonNullPlant ->
                    val nameTextView = findViewById<TextView>(R.id.plantName)
                    val remindersTextView = findViewById<TextView>(R.id.reminders)
                    val imageView = findViewById<ImageView>(R.id.imageView)

                    nameTextView.text = nonNullPlant.name
                    remindersTextView.text = nonNullPlant.date

                    if (!nonNullPlant.imagepath.isNullOrEmpty()) {
                        try {
                            val file = File(nonNullPlant.imagepath)
                            if (file.exists()) {
                                val inputStream = FileInputStream(file)
                                val bitmap = BitmapFactory.decodeStream(inputStream)
                                imageView.setImageBitmap(bitmap)
                                inputStream.close()
                            } else {
                                imageView.setImageResource(R.drawable.add_plant) // DefaultImg
                            }
                        } catch (e: FileNotFoundException) {
                            e.printStackTrace()
                            imageView.setImageResource(R.drawable.add_plant) // file is not found
                        }
                    } else {
                        imageView.setImageResource(R.drawable.add_plant) // there is no image path
                    }
                    getEntries(nonNullPlant.name)
                }
            }
        }
    }

    private fun initRecyclerView() {
        dateEntryRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = DateEntryAdapter()
        dateEntryRecyclerView.adapter = adapter
    }

    private fun getEntries(plantName: String) {
        val dateEntryList = sqLiteHelper.getEntriesForPlantName(plantName)
        Log.e("Show added plant", "${dateEntryList.size}")
      //  Log.e("Show added plant", "Number of entries for plant $plantId: ${dateEntryList.size}")


        // Test to see if actually populating with a recyclerview
        adapter?.addItems(ArrayList(dateEntryList))
    }


}



