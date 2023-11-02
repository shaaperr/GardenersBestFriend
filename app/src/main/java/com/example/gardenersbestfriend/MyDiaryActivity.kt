package com.example.gardenersbestfriend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class MyDiaryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_diary)

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
}