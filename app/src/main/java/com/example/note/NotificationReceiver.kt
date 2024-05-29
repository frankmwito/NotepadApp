package com.example.note

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.util.Log

class NotificationReceiver : BroadcastReceiver() {
    private val LOG_TAG = "NotificationReceiver"

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            when (it.action) {
                "STOP_RINGTONE" -> {
                    Log.d(LOG_TAG, "Stop action received")
                    val notificationId = it.getIntExtra("notificationId", -1)
                    val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.cancel(notificationId)

                    val ringtoneUri = it.getStringExtra("ringtoneUri")?.let { Uri.parse(it) }
                    ringtoneUri?.let { ringtone ->
                        val ringtone = RingtoneManager.getRingtone(context, ringtone)
                        if (ringtone.isPlaying) {
                            ringtone.stop()
                            Log.d(LOG_TAG, "Ringtone stopped")
                        } else {
                            Log.d(LOG_TAG, "Ringtone not playing")
                        }
                    } ?: run {
                        Log.d(LOG_TAG, "Ringtone URI is null")
                    }
                }

                else -> {}
            }
        }
    }
}
