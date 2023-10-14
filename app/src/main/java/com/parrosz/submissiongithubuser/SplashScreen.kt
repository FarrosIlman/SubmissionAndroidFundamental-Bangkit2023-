package com.parrosz.submissiongithubuser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ProgressBar
import android.widget.TextView
import com.parrosz.submissiongithubuser.ui.MainActivity

class SplashScreen : AppCompatActivity() {
    private val SPLASH_TIMEOUT: Long = 2000 // 2 detik

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        val progressBar: ProgressBar = findViewById(R.id.progressBar)
        val loadingText: TextView = findViewById(R.id.loadingText)

        progressBar.visibility = ProgressBar.VISIBLE
        val slideInAnimation: Animation = AnimationUtils.loadAnimation(this, R.anim.slide_in)
        loadingText.startAnimation(slideInAnimation)
        loadingText.visibility = TextView.VISIBLE

        Handler().postDelayed({
            val intent = Intent(this@SplashScreen, MainActivity::class.java)
            startActivity(intent)

            finish()
        }, SPLASH_TIMEOUT)
    }
}