package com.example.gardenersbestfriend

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import kotlin.random.Random



class MainActivity : AppCompatActivity() {

    companion object {
        private const val WRITE_EXTERNAL_STORAGE = 100
        private const val READ_EXTERNAL_STORAGE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermission(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            READ_EXTERNAL_STORAGE)

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
    //permission testing
    private fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(this@MainActivity, permission) == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            Log.d("TAG", "Requesting permission: $permission")
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(permission), requestCode)
        } else {
            Log.d("TAG", "Permission already granted: $permission")
            //Toast.makeText(this@MainActivity, "Permission already granted", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            READ_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("TAG", "Read Permission Granted")
                    Toast.makeText(this@MainActivity, "Read Permission Granted", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d("TAG", "Read Permission Denied")
                    Toast.makeText(this@MainActivity, "Read Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
            WRITE_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("TAG", "Write Permission Granted")
                    Toast.makeText(this@MainActivity, "Write Permission Granted", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d("TAG", "Write Permission Denied")
                    Toast.makeText(this@MainActivity, "Write Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}