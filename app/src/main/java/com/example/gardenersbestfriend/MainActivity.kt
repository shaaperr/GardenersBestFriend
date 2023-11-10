package com.example.gardenersbestfriend

import android.Manifest
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import kotlin.random.Random



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermissions()

        displayRandomEntry()

        val addPlantButton:ImageButton=findViewById(R.id.addplant_button)
        addPlantButton.setOnClickListener{
            startActivity(Intent(this,AddNewPlantActivity::class.java))
        }

        val imageButton1:ImageButton=findViewById(R.id.home_button)
        imageButton1.setOnClickListener{
            startActivity(Intent(this,MainActivity::class.java))
        }

        val imageButton2:ImageButton=findViewById(R.id.mydiary_button)
        imageButton2.setOnClickListener{
            startActivity(Intent(this,MyDiaryActivity::class.java))
        }

        val imageButton3:ImageButton=findViewById(R.id.history_button)
        imageButton3.setOnClickListener{
            startActivity(Intent(this,MyHistoryActivity::class.java))
        }

    }

    private fun displayRandomEntry() {
        val sqLiteHelper = SQLiteHelper(this)
        val plantList = sqLiteHelper.getAllPlant()

        if (plantList.isNotEmpty()) {
            val randomIndex = Random.nextInt(plantList.size)
            val randomPlant = plantList[randomIndex]

            val plantNameTextView: TextView = findViewById(R.id.plantName)
            val randomImageView: ImageView = findViewById(R.id.imageView)

            plantNameTextView.text = randomPlant.name

            if (!randomPlant.imagepath.isNullOrEmpty()) {
                try {
                    val file = File(randomPlant.imagepath)
                    if (file.exists()) {
                        val inputStream = FileInputStream(file)
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        randomImageView.setImageBitmap(bitmap)
                        inputStream.close()
                    } else {
                        // Handle the case where the file is not found
                        randomImageView.setImageResource(R.drawable.add_plant) // You can set a default image
                    }
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                    randomImageView.setImageResource(R.drawable.add_plant) // Handle the case where the file is not found
                }
            } else {
                randomImageView.setImageResource(R.drawable.add_plant) // Handle the case where there is no image path
            }
        }
    }

    private fun checkPermissions() {
        var permissions: Array<String>
        if (android.os.Build.VERSION.SDK_INT >= 33) {
            permissions = arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_MEDIA_IMAGES
            ) // add any needed permissions
        }
        else if (android.os.Build.VERSION.SDK_INT <= 28){
            permissions = arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
        else {
            permissions = arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
        requestPermissions.launch(permissions)
    }


    private val requestPermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        results ->
        val TAG = "PermissionCheck"
        for (isGranted in results.values) {
            Log.d(TAG, "Permission granted: $isGranted")
        }
    }
}