package com.example.note

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import androidx.work.WorkManager

// MyApp.kt
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize WorkManager
        WorkManager.initialize(
            this,
            Configuration.Builder().setMinimumLoggingLevel(Log.DEBUG).build()
        )
    }
}
