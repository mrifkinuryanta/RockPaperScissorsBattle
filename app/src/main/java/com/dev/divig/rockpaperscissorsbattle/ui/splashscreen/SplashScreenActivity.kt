package com.dev.divig.rockpaperscissorsbattle.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dev.divig.rockpaperscissorsbattle.ui.menu.GameMenuActivity

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this@SplashScreenActivity, GameMenuActivity::class.java)
        startActivity(intent)
        finish()
    }
}