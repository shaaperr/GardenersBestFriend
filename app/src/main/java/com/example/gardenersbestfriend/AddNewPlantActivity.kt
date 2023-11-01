package com.example.gardenersbestfriend

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar
class AddNewPlantActivity : AppCompatActivity() {
    val daysOfWeek = arrayOf("Mondays", "Tuesdays", "Wednesdays", "Thursdays", "Fridays", "Saturdays", "Sundays")
    lateinit var selectedDay: BooleanArray
    lateinit var textView: TextView
    private val selectedItems = HashSet<Int>() // Keep track of selected items

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_plant)

        val notificationButton = findViewById<Button>(R.id.submitButton) //CAN BE USED TO POPULATE DATABASE(?) Currently sets reminders
        notificationButton.setOnClickListener {
            val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
            val intent = Intent(this, Reminders::class.java)
            val now = Calendar.getInstance()

            // Create a set to keep track of the alarms to be scheduled
            val alarmsToSchedule = HashSet<Int>()

            // Schedule alarms for the selected days
            for (dayOfWeek in selectedItems) {
                val selectedDayString = daysOfWeek[dayOfWeek]
                val today = Calendar.getInstance()
                val dayOfWeekToday = today.get(Calendar.DAY_OF_WEEK)
                val daysUntilSelectedDay = (getDayOfWeekValue(selectedDayString) - dayOfWeekToday + 7) % 7

                // Calculate the next occurrence for the selected day
                today.add(Calendar.DAY_OF_YEAR, daysUntilSelectedDay)
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = today.timeInMillis
                calendar.set(Calendar.HOUR_OF_DAY, 20) //Time alarm will go off for each upcoming day
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)

                // Check if the time is in the past on the same day, add 7 days if it is
                if (today.get(Calendar.DAY_OF_WEEK) == dayOfWeekToday && calendar.before(now)) {
                    calendar.add(Calendar.DAY_OF_YEAR, 7)
                }

                val scheduledTime = calendar.time
                Log.d("AlarmScheduling", "Scheduled time for $selectedDayString: $scheduledTime")

                // Create a new PendingIntent to ensure a new alarm is scheduled
                val newPendingIntent = PendingIntent.getBroadcast(
                    this, dayOfWeek, intent, PendingIntent.FLAG_UPDATE_CURRENT
                )

                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY * 7, // Ensures that it repeats every week.
                    newPendingIntent
                )

                alarmsToSchedule.add(dayOfWeek)
            }

            // Cancel alarms for unselected days, ends up cancelling alarms that don't exist might work on it more
            for (dayOfWeek in (0 until 7).toSet() - alarmsToSchedule) {
                val pendingIntent = PendingIntent.getBroadcast(
                    this, dayOfWeek, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
                alarmManager.cancel(pendingIntent)
                Log.d("AlarmCancelling", "Canceled alarm for ${daysOfWeek[dayOfWeek]}")
            }
        }

        //Drop down menu, populates textview
        textView = findViewById(R.id.waterScheduleText)
        textView.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Set Weekly Reminders")
            builder.setCancelable(false)

            selectedDay = BooleanArray(daysOfWeek.size) { selectedItems.contains(it) }

            builder.setMultiChoiceItems(daysOfWeek, selectedDay) { _, i, b ->
                if (b) {
                    selectedItems.add(i)
                } else {
                    selectedItems.remove(i)
                }
            }

            builder.setPositiveButton("Ok") { _, _ ->
                val stringBuilder = StringBuilder()

                for (item in selectedItems) {
                    stringBuilder.append(daysOfWeek[item])
                    stringBuilder.append(", ")
                }

                if (stringBuilder.isNotEmpty()) {
                    stringBuilder.setLength(stringBuilder.length - 2)
                }

                textView.text = stringBuilder.toString()
            }

            builder.setNegativeButton("Cancel") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }

            builder.setNeutralButton("Clear all") { _, _ ->
                selectedItems.clear()
                textView.text = ""
            }
            builder.show()
        }

        val plantImagePreview: ImageButton = findViewById(R.id.plantImagePreview)
        var plantUri: Uri? = null
        val pickImage = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            val tag = "ImagePicker"
            if (uri != null) {
                plantImagePreview.setImageURI(uri)
                plantUri = uri
                Log.d(tag, "Selected image: $uri")

            } else {
                Log.d(tag, "No image selected")
            }
        }

        val addPlantImage: PopupMenu = PopupMenu(this, plantImagePreview)
        addPlantImage.inflate(R.menu.popup_add_plant_image)

        plantImagePreview.setOnClickListener{
            addPlantImage.show()
        }

        addPlantImage.setOnMenuItemClickListener { menuItem ->
            val id = menuItem.itemId
            if (id == R.id.useCamera) {
                // TODO: open camera
            }
            else if (id == R.id.openGallery) {
                pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
            false
        }
    }
}


private fun getDayOfWeekValue(dayString: String): Int {
    return when (dayString) {
        "Sundays" -> Calendar.SUNDAY
        "Mondays" -> Calendar.MONDAY
        "Tuesdays" -> Calendar.TUESDAY
        "Wednesdays" -> Calendar.WEDNESDAY
        "Thursdays" -> Calendar.THURSDAY
        "Fridays" -> Calendar.FRIDAY
        "Saturdays" -> Calendar.SATURDAY
        else -> -1 // Invalid value
    }
}



