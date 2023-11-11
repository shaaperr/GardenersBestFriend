package com.example.gardenersbestfriend

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.FileNotFoundException

class PlantAdapter : RecyclerView.Adapter<PlantAdapter.PlantViewHolder>() {
    private var plantList: ArrayList<PlantModel> = ArrayList()
    private var onClickItem:((PlantModel)->Unit)?=null

    fun addItems(items: ArrayList<PlantModel>) {
        this.plantList = items
        notifyDataSetChanged()
    }

    fun setOnclickItem(callback: (PlantModel)->Unit){
        this.onClickItem = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PlantViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.card_items_plants, parent, false)
    )

    override fun getItemCount(): Int {
        return plantList.size
    }

    override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
        val plant = plantList[position]
        holder.bindView(plant)
    }

    class PlantViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        private var name = view.findViewById<TextView>(R.id.tvName)
        private var date = view.findViewById<TextView>(R.id.tvDate)
        private var reminders = view.findViewById<TextView>(R.id.tvReminders)
        private var imageView = view.findViewById<ImageView>(R.id.imageView)
        private var id = view.findViewById<Button>(R.id.tvId)

        fun bindView(plant: PlantModel) {
            name.text = plant.name
            date.text = plant.date
            reminders.text = plant.reminders

            // Check if the image path is not empty
            if (!plant.imagepath.isNullOrEmpty()) {
                val fileUri = Uri.parse("file://${plant.imagepath}") // Create a file URI
                val contentResolver = view.context.contentResolver
                try {
                    val inputStream = contentResolver.openInputStream(fileUri)
                    if (inputStream != null) {
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        imageView.setImageBitmap(bitmap)
                        inputStream.close()
                    } else {
                        // Handle the case where the input stream is null
                        Log.e("PlantViewHolder", "Input stream is null for plant ID: ${plant.id}")
                        imageView.setImageResource(R.drawable.add_plant) // set a default image, i picked randomly
                    }
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                    Log.e("PlantViewHolder", "File not found for plant ID: ${plant.id}")
                    imageView.setImageResource(R.drawable.add_plant) // Handle the case where the file is not found
                }
            } else {
                Log.e("PlantViewHolder", "Image path is null or empty for plant ID: ${plant.id}")
                imageView.setImageResource(R.drawable.add_plant) // Handle the case where there is no image path
            }


            id.setOnClickListener {
                val intent = Intent(view.context, SinglePlantActivity::class.java)
                intent.putExtra("plant_id", plant.id) // Pass the selected plant's data to the detail activity
               Log.d("PlantAdapter", "Selected Plant ID: ${plant.id}") // Add this log statement
               view.context.startActivity(intent)

            }


        }
    }
}

