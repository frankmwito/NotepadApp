package com.example.note

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class NotificationWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        val title = inputData.getString("title")
        val description = inputData.getString("description")
        val ringtoneUriString = inputData.getString("ringtone")
        val ringtoneUri = ringtoneUriString?.let { Uri.parse(it) }

        showNotification(title, description, ringtoneUri)

        return Result.success()
    }

    private fun showNotification(title: String?, description: String?, ringtoneUri: Uri?) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create a notification channel if it doesn't exist
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("todo_channel", "To-Do Notifications", NotificationManager.IMPORTANCE_HIGH).apply {
                this.description = "Channel for to-do notifications"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(applicationContext, "todo_channel")
            .setContentTitle(title)
            .setContentText(description)
            .setSmallIcon(R.drawable.ic_notification)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        // Set a delete intent to stop the ringtone when the notification is dismissed
        val stopRingtoneIntent = Intent(applicationContext, NotificationReceiver::class.java).apply {
            action = "STOP_RINGTONE"
            putExtra("notificationId", 1)
            putExtra("ringtoneUri", ringtoneUri.toString())
        }
        val stopRingtonePendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            0,
            stopRingtoneIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        notificationBuilder.setDeleteIntent(stopRingtonePendingIntent)

        // Add a delete action to the notification
        val deleteIntent = Intent(applicationContext, NotificationReceiver::class.java).apply {
            action = "DELETE_NOTIFICATION"
            putExtra("notificationId", 1)
        }
        val deletePendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            0,
            deleteIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        notificationBuilder.addAction(R.drawable.ic_stop, "Stop", deletePendingIntent)

        notificationManager.notify(1, notificationBuilder.build())

        // Play ringtone
        ringtoneUri?.let {
            val ringtone = RingtoneManager.getRingtone(applicationContext, it)
            ringtone.play()
        }
    }
}

