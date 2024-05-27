package com.example.note

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "STOP_RINGTONE") {
            val notificationId = intent.getIntExtra("notificationId", -1)
            val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(notificationId)
            // Stop the ringtone here if it's playing
            val ringtoneUri = intent.getStringExtra("ringtoneUri")?.let { Uri.parse(it) }
            if (ringtoneUri != null) {
                val ringtone = RingtoneManager.getRingtone(context, ringtoneUri)
                if (ringtone.isPlaying) {
                    ringtone.stop()
                }
            }
        }
    }
}