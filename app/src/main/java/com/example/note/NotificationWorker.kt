package com.example.note

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class NotificationWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val title = inputData.getString("title") ?: return Result.failure()
        val description = inputData.getString("description") ?: return Result.failure()
        val ringtoneUri = inputData.getString("ringtone")?.let { Uri.parse(it) }

        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = System.currentTimeMillis().toInt()

        val stopIntent = Intent(applicationContext, NotificationReceiver::class.java).apply {
            action = "STOP_RINGTONE"
            putExtra("notificationId", notificationId)
        }
        val stopPendingIntent = PendingIntent.getBroadcast(applicationContext, notificationId, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(applicationContext, "todo_channel")
            .setContentTitle(title)
            .setContentText(description)
            .setSmallIcon(R.drawable.ic_notification)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setSound(ringtoneUri)
            .addAction(R.drawable.ic_stop, "Stop", stopPendingIntent)
            .build()

        notificationManager.notify(notificationId, notification)

        return Result.success()
    }
}

