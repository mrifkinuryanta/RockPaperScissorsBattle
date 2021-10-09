package com.dev.divig.rockpaperscissorsbattle.ui.intro

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.dev.divig.rockpaperscissorsbattle.R
import com.dev.divig.rockpaperscissorsbattle.enum.LandingPage
import com.github.appintro.AppIntro2

class GameIntroActivity : AppIntro2() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isSkipButtonEnabled = false
        isWizardMode = true

        setIndicatorColor(
            selectedIndicatorColor = ContextCompat.getColor(this, R.color.orange_100),
            unselectedIndicatorColor = ContextCompat.getColor(this, R.color.brown_500)
        )

        addSlide(LandingPageFragment(LandingPage.FIRST))
        addSlide(LandingPageFragment(LandingPage.SECOND))
        addSlide(LandingPageFragment(LandingPage.THIRD))
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        if (currentFragment is LandingPageFragment) {
            currentFragment.navigateToGameMenu()
        }
    }
}