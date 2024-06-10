package com.example.note

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import androidx.work.WorkManager

// MyApp.kt
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize WorkManager with the default configuration
        WorkManager.initialize(this, Configuration.Builder().build())
    }
}
