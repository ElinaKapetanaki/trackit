package com.trackit

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

class VideoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        val videoView: VideoView = findViewById(R.id.videoView)

        val videoUri = Uri.parse("android.resource://${packageName}/${R.raw.video}")
        videoView.setVideoURI(videoUri)
        videoView.setOnErrorListener { _, what, extra ->
            Log.e("VideoActivity", "Video playback error: what=$what, extra=$extra")
            finish()
            true
        }


        videoView.setOnCompletionListener {
            finish()
        }

        videoView.start()
    }
}
