package com.example.gardenersbestfriend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PlantAdapter: RecyclerView.Adapter<PlantAdapter.PlantViewHolder>() {
    private var plantList:ArrayList<PlantModel> = ArrayList()

    fun addItems(items:ArrayList<PlantModel>){
        this.plantList = items
        notifyDataSetChanged()
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

    class PlantViewHolder(var view: View): RecyclerView.ViewHolder(view){
       // private var id = view.findViewById<TextView>(R.id.tvId)
        private var name = view.findViewById<TextView>(R.id.tvName)
        private var date = view.findViewById<TextView>(R.id.tvDate)
        private var reminders = view.findViewById<TextView>(R.id.tvReminders)
       // private var entry = view.findViewById<TextView>(R.id.tvEntry)
        private var placeholder = view.findViewById<Button>(R.id.PlaceHolder)





        fun bindView(plant:PlantModel){
           // id.text = plant.id.toString()
            name.text = plant.name
            date.text = plant.date
            reminders.text = plant.reminders
           // entry.text = plant.entry
            name.text = plant.name
        }
    }
}