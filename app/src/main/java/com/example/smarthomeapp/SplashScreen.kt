package com.example.smarthomeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val titleAnimation = AnimationUtils.loadAnimation(this, R.anim.title_animation)
        val subtitleAnimation = AnimationUtils.loadAnimation(this, R.anim.subtitle_animation)

        val title = findViewById<TextView>(R.id.smartHavenTitle)
        val subtitle = findViewById<TextView>(R.id.subtitle)

        title.startAnimation(titleAnimation)
        subtitle.startAnimation(subtitleAnimation)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 4000)
    }

}