package com.example.gardenersbestfriend

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException

class SinglePlantActivity : AppCompatActivity() {

    private lateinit var sqLiteHelper: SQLiteHelper
    private var plant: PlantModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_plant)

        sqLiteHelper = SQLiteHelper(this)

        val plantId = intent.getIntExtra("plant_id", -1)

        if (plantId != -1) {
            // Fix: Remove the 'val' keyword to use the existing member variable 'plant'
            plant = sqLiteHelper.getOneById(plantId)

            plant?.let { nonNullPlant ->
                val nameTextView = findViewById<TextView>(R.id.plantName)
                val dateTextView = findViewById<TextView>(R.id.date)
                val remindersTextView = findViewById<TextView>(R.id.reminders)
                val entryTextView = findViewById<TextView>(R.id.entry)
                val imageView = findViewById<ImageView>(R.id.imageView)

                nameTextView.text = nonNullPlant.name
                dateTextView.text = nonNullPlant.reminders
                remindersTextView.text = nonNullPlant.date
                entryTextView.text = nonNullPlant.entry

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
}



