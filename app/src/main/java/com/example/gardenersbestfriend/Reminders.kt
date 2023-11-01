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
const val titleExtra = "titleExtra"
const val messageExtra = "MessageExtra"
class Reminders : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("NotificationReceiver", "Notification received")

        if (context != null) {
            // Create a notification channel (required for Android 8.0 and higher)
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
                .setContentText("Don't forget to water your plant today!")
                .setAutoCancel(true)
                .build()

            // Get the NotificationManager and notify the user
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.notify(1, notification)
        }
        Log.d("NotificationReceiver", "Notification displayed")

    }
}