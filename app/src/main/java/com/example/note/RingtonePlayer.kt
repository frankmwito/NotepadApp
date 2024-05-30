package com.example.note

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri

object RingtonePlayer {
    private var mediaPlayer: MediaPlayer? = null

    fun play(context: Context, uri: Uri) {
        stop() // Stop any currently playing ringtone
        mediaPlayer = MediaPlayer().apply {
            setDataSource(context, uri)
            isLooping = true // Set looping to true
            setOnPreparedListener {
                it.start()
            }
            prepareAsync() // Prepare the MediaPlayer asynchronously
        }
    }

    fun stop() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
        }
        mediaPlayer = null
    }
}