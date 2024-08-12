package com.example.note

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class NotificationWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        val title = inputData.getString("title")
        val description = inputData.getString("description")
        val ringtoneUriString = inputData.getString("ringtone")
        val ringtoneUri = ringtoneUriString?.let { Uri.parse(it) }

        showNotification(title, description, ringtoneUri, ringtoneUriString)

        return Result.success()
    }

    private fun showNotification(title: String?, noteDescription: String?, ringtoneUri: Uri?, ringtoneUriString: String?) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create a notification channel if it doesn't exist
        val channel = NotificationChannel("todo_channel", "To-Do Notifications", NotificationManager.IMPORTANCE_HIGH).apply {
            this.description = "Channel for to-do notifications"
            enableLights(true)
            lightColor = Color.RED
            enableVibration(true)
        }
        notificationManager.createNotificationChannel(channel)

        val notificationId = 1 // Use a unique ID for each notification

        // Stop Ringtone Intent
        val stopRingtoneIntent = Intent(applicationContext, NotificationReceiver::class.java).apply {
            action = "STOP_RINGTONE"
            putExtra("notificationId", notificationId)
        }
        val stopRingtonePendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            0,
            stopRingtoneIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Remind Later Intent
        val remindLaterIntent = Intent(applicationContext, NotificationReceiver::class.java).apply {
            action = "REMIND_LATER"
            putExtra("notificationId", notificationId)
            putExtra("title", title)
            putExtra("description", noteDescription)  // Updated to use noteDescription
            putExtra("ringtone", ringtoneUriString)
        }
        val remindLaterPendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            1,
            remindLaterIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Build Notification
        val notificationBuilder = NotificationCompat.Builder(applicationContext, "todo_channel")
            .setContentTitle(title)
            .setContentText(noteDescription)  // Updated to use noteDescription
            .setSmallIcon(R.drawable.ic_notification)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setDeleteIntent(stopRingtonePendingIntent) // Trigger stop when notification is dismissed
            .addAction(R.drawable.ic_stop, "Stop", stopRingtonePendingIntent) // Action button to stop
            .addAction(R.drawable.ic_notification, "Remind me in 10 minutes", remindLaterPendingIntent) // Action button to remind later
            .setDefaults(NotificationCompat.DEFAULT_ALL) // Set default notification behaviors
            .setCategory(NotificationCompat.CATEGORY_ALARM) // Set category to alarm
            .setFullScreenIntent(
                PendingIntent.getActivity(
                    applicationContext,
                    0,
                    Intent(),
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                ), true
            )

        notificationManager.notify(notificationId, notificationBuilder.build())
        // Play Ringtone
        ringtoneUri?.let {
            RingtonePlayer.play(applicationContext, it)
        }
    }

}
