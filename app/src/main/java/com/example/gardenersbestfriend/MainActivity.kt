package com.example.gardenersbestfriend

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

        checkPermissions()
    }

    private fun checkPermissions() {
        var permissions: Array<String>
        if (android.os.Build.VERSION.SDK_INT > 28) {
            permissions = arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_MEDIA_IMAGES
            ) // add any needed permissions
        }
        else {
            permissions = arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
        requestPermissions.launch(permissions)

    }

    val requestPermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
        val tag = "permissionCheck"
        for (isGranted in results.values) {
            Log.d(tag, "requestPermissions: permission granted = $isGranted")
        }
    }
}