package com.example.gardenersbestfriend

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MyDiaryActivity : AppCompatActivity() {
    private lateinit var sqLiteHelper: SQLiteHelper
    private lateinit var recyclerView: RecyclerView
    private var adapter: PlantAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_diary)

        // Initialize the recyclerView
        recyclerView = findViewById(R.id.recyclerViewPlants)
        initRecyclerView()
        sqLiteHelper = SQLiteHelper(this)
        getPlants()

        adapter?.setOnclickItem {
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show()

        }


        val imageButton1: ImageButton = findViewById(R.id.home_button)
        imageButton1.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        val imageButton2: ImageButton = findViewById(R.id.mydiary_button)
        imageButton2.setOnClickListener {
            startActivity(Intent(this, MyDiaryActivity::class.java))
        }

        val imageButton3: ImageButton = findViewById(R.id.history_button)
        imageButton3.setOnClickListener {
            startActivity(Intent(this, MyHistoryActivity::class.java))
        }



    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = PlantAdapter()
        recyclerView.adapter = adapter

    }

    private fun getPlants() {
        val plantList = sqLiteHelper.getAllPlant()
        Log.e("Show added plant", "${plantList.size}")

        for (plant in plantList) {
            Log.d("addPlantReminders", "Plant Id: ${plant.id}, Plant Name: ${plant.name}, Reminders: ${plant.reminders}")
        }


        // Test to see if actually populating with a recyclerview
        adapter?.addItems(plantList)
    }


}



