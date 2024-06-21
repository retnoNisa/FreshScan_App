package com.example.freshscanapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.freshscanapp.R

class SplashScreenActivity : AppCompatActivity() {
    private val splashTimeOut: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val imageViewSplash: ImageView = findViewById(R.id.leafImage)
        val rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.zoom)
        imageViewSplash.startAnimation(rotateAnimation)

        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, splashTimeOut)
    }
}