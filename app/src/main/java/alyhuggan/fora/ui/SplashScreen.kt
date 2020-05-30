package alyhuggan.fora.ui

import alyhuggan.fora.R
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SplashScreen: AppCompatActivity() {

    private val SPLASH_TIME: Long = 3000
    private val TEXT_ANIM_DELAY: Long = 1200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val logo = findViewById<ImageView>(R.id.splash_logo)
        val text = findViewById<TextView>(R.id.splash_description)

        val animateLogo = AnimationUtils.loadAnimation(this, R.anim.scale_up)
        val animateText = AnimationUtils.loadAnimation(this, R.anim.slide)

        logo.startAnimation(animateLogo)

        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, SPLASH_TIME)

        Handler().postDelayed({
            text.visibility = View.VISIBLE
            text.startAnimation(animateText)
        }, TEXT_ANIM_DELAY)
    }

}