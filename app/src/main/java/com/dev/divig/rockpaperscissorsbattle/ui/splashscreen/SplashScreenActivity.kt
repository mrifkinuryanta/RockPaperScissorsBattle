package com.dev.divig.rockpaperscissorsbattle.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dev.divig.rockpaperscissorsbattle.ui.intro.GameIntroActivity

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, GameIntroActivity::class.java)
        startActivity(intent)
        finish()
    }
}