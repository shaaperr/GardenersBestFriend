package com.example.gardenersbestfriend

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat


const val notificationID = 1
const val channelID = "Channel"
const val messageExtra = "MessageExtra"
class Reminders : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("NotificationReceiver", "Notification received")

        if (context != null && intent != null) {
            // Retrieve extras (plant name and custom message) from the intent
            val plantName = intent.getStringExtra(messageExtra)

            if (plantName != null) {
                // The value of 'messageExtra' has been successfully retrieved
                Log.d("PlantNameFromIntent", "Plant Name from Intent: $plantName")

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    val channelId = "my_notification_channel"
                    val channelName = "My Notification Channel"
                    val importance = NotificationManager.IMPORTANCE_HIGH
                    val channel = NotificationChannel(channelId, channelName, importance)

                    val notificationManager = context.getSystemService(NotificationManager::class.java)
                    notificationManager.createNotificationChannel(channel)
                }

                // Build the notification
                val notification = NotificationCompat.Builder(context, "my_notification_channel")
                    .setSmallIcon(R.drawable.my_diary) // Change to your notification icon
                    .setContentTitle("Water Your Plant")
                    .setContentText("Don't forget to water your $plantName today!")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .build()

                // Get the NotificationManager and notify the user
                val notificationManager = context.getSystemService(NotificationManager::class.java)
                notificationManager.notify(1, notification)


            }else {
                // 'messageExtra' value is not present in the Intent or is null
                Log.d("PlantNameFromIntent", "Plant Name from Intent is null")
            }

              /*  // The value of 'messageExtra' has been successfully retrieved
                Log.d("PlantNameFromIntent", "Plant Name from Intent: $plantName")

                // Build the custom notification with 'plantName'
                val notification = NotificationCompat.Builder(context, "my_notification_channel")
                    .setSmallIcon(R.drawable.my_diary)
                    .setContentTitle("Water Your Plant")
                    .setContentText("Don't forget to water your $plantName today!")
                    .setAutoCancel(true)
                    .build()

                // Get the NotificationManager and notify the user
                val notificationManager = context.getSystemService(NotificationManager::class.java)
                notificationManager.notify(notificationID, notification)
            } else {
                // 'messageExtra' value is not present in the Intent or is null
                Log.d("PlantNameFromIntent", "Plant Name from Intent is null")*/

        }
        Log.d("NotificationReceiver", "Notification displayed")
    }

}
