package com.camilo.appcompras.start

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.camilo.appcompras.MainActivity
import com.camilo.appcompras.R
import com.camilo.appcompras.auth.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val mAuth = FirebaseAuth.getInstance()
        val splashTimeOut: Long = 2600
        val logo = findViewById<ImageView>(R.id.logo)

        // Set up the animations
        val animatorTranslate =
            ObjectAnimator.ofFloat(logo, "translationX", 0f, 1000f) // Move right
        animatorTranslate.duration = 1000
        animatorTranslate.startDelay = 1000


        val animatorFadeOut = ObjectAnimator.ofFloat(logo, "alpha", 1f, 0f)
        animatorFadeOut.duration = 500

        val animatorSet = AnimatorSet()
        animatorSet.play(animatorTranslate).before(animatorFadeOut)

        animatorSet.start()

        animatorSet.addListener(object : android.animation.AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: android.animation.Animator) {
                super.onAnimationEnd(animation)

                if (mAuth.currentUser != null) {
                    val intent = Intent(this@StartActivity, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    val intent = Intent(this@StartActivity, LoginActivity::class.java)
                    startActivity(intent)
                }
                finish()
            }
        })
    }
}
