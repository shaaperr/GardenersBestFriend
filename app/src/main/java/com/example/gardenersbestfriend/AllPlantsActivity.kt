package com.example.gardenersbestfriend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class AllPlantsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_plants)



        val imageButton2: ImageButton =findViewById(R.id.mydiary_button)
        imageButton2.setOnClickListener{
            startActivity(Intent(this,MyDiaryActivity::class.java))
        }

        val imageButton3: ImageButton =findViewById(R.id.history_button)
        imageButton3.setOnClickListener{
            startActivity(Intent(this,MyHistoryActivity::class.java))
        }
    }
}