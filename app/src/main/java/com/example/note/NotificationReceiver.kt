package com.example.note

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

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
        }
    }
}


