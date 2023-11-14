package com.example.gardenersbestfriend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class DateEntryAdapter : RecyclerView.Adapter<DateEntryAdapter.DateEntryViewHolder>() {
    private var dateEntryList: ArrayList<DateEntryModel> = ArrayList()

    // Updated addItems function to sort the list by date before notifying the adapter
    fun addItems(items: ArrayList<DateEntryModel>) {
        this.dateEntryList = items
        // Sort the list by date in descending order
        dateEntryList.sortByDescending { it.date }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DateEntryViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_date_entry, parent, false)
    )

    override fun getItemCount(): Int {
        return dateEntryList.size
    }

    override fun onBindViewHolder(holder: DateEntryViewHolder, position: Int) {
        val dateEntry = dateEntryList[position]
        holder.bindView(dateEntry)
    }

    class DateEntryViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        private var date = view.findViewById<TextView>(R.id.tvDate)
        private var entry = view.findViewById<TextView>(R.id.tvEntry)

        fun bindView(dateEntry: DateEntryModel) {
            date.text = dateEntry.date
            entry.text = dateEntry.entry
        }
    }
}
