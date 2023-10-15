package com.example.gardenersbestfriend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class MyHistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_history)


        val imageButton1: ImageButton =findViewById(R.id.home_button)
        imageButton1.setOnClickListener{
            startActivity(Intent(this,AllPlantsActivity::class.java))
        }

        val imageButton2: ImageButton =findViewById(R.id.mydiary_button)
        imageButton2.setOnClickListener{
            startActivity(Intent(this,MyDiaryActivity::class.java))
        }


    }
}