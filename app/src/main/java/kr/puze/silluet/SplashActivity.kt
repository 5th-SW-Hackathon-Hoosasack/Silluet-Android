package kr.puze.silluet

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
class SplashActivity : AppCompatActivity() {

    lateinit var anim: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)
        supportActionBar!!.hide()

        val hd1 = Handler()
        val hd2 = Handler()
        startAnimations()
        hd1.postDelayed(Runnable {
            endAnimation()
            hd2.postDelayed(Runnable {
                val animlayout = findViewById<ImageView>(R.id.layout_splash)
                animlayout.visibility = View.INVISIBLE
                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }, 2500)
        }, 2500)
    }

    private fun startAnimations(): Unit{
        var animaLayout: ImageView = findViewById(R.id.layout_splash)
        anim = AnimationUtils.loadAnimation(this@SplashActivity, R.anim.alpha)
        anim.reset()
        animaLayout.clearAnimation()
        animaLayout.startAnimation(anim)
    }

    private fun endAnimation(): Unit{
        var animaLayout: ImageView = findViewById(R.id.layout_splash)
        anim = AnimationUtils.loadAnimation(this@SplashActivity, R.anim.disalpha)
        anim.reset()
        animaLayout.clearAnimation()
        animaLayout.startAnimation(anim)
    }
}