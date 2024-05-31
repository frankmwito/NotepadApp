package com.example.note

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import java.util.concurrent.TimeUnit

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "STOP_RINGTONE") {
            Log.d("NotificationReceiver", "Stop action received")
            val notificationId = intent.getIntExtra("notificationId", -1)
            val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(notificationId)

            // Stop the ringtone
            RingtonePlayer.stop()
            Log.d("NotificationReceiver", "Ringtone stopped")
        } else if (intent?.action == "REMIND_LATER") {
            Log.d("NotificationReceiver", "Remind later action received")
            val notificationId = intent.getIntExtra("notificationId", -1)
            val title = intent.getStringExtra("title")
            val description = intent.getStringExtra("description")
            val ringtoneUriString = intent.getStringExtra("ringtone")

            val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(notificationId)

            // Stop the ringtone
            RingtonePlayer.stop()
            Log.d("NotificationReceiver", "Ringtone stopped")

            // Schedule a new notification for 10 minutes later
            val remindLaterWorkRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInputData(
                    workDataOf(
                        "title" to title,
                        "description" to description,
                        "ringtone" to ringtoneUriString
                    )
                )
                .setInitialDelay(10, TimeUnit.MINUTES)
                .build()
            WorkManager.getInstance(context).enqueue(remindLaterWorkRequest)
        }
    }
}






