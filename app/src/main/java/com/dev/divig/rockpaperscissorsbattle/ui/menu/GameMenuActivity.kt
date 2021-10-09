package com.dev.divig.rockpaperscissorsbattle.ui.menu

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.dev.divig.rockpaperscissorsbattle.R
import com.dev.divig.rockpaperscissorsbattle.data.preference.GamePreference
import com.dev.divig.rockpaperscissorsbattle.databinding.ActivityGameMenuBinding
import com.dev.divig.rockpaperscissorsbattle.enum.GameMenuDialog
import com.dev.divig.rockpaperscissorsbattle.enum.GameMode
import com.dev.divig.rockpaperscissorsbattle.ui.game.GameActivity
import com.dev.divig.rockpaperscissorsbattle.utils.Constants
import com.dev.divig.rockpaperscissorsbattle.utils.Utils

class GameMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameMenuBinding
    private lateinit var gamePreference: GamePreference

    private var isFirstRunApp: Boolean = true
    private var namePlayerOne: String = Constants.EMPTY_VALUE
    private var namePlayerTwo: String = Constants.EMPTY_VALUE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setClickAction()
    }

    private fun init() {
        binding = ActivityGameMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        gamePreference = GamePreference(this)

        binding.ivGameLogo.load(Constants.URL_LOGO) {
            placeholder(R.drawable.img_placeholder)
            error(R.drawable.img_placeholder)
        }

        getPrefsData()

        val wrd = getString(R.string.msg_welcome, namePlayerOne)
        Utils.showSnackBar(this, binding.root, wrd)
    }

    private fun setClickAction() {
        binding.btnVersusPlayer.setOnClickListener {
            getPrefsData()
            if (isFirstRunApp && namePlayerTwo.isEmpty()) showNotificationDialog() else navigateToGame(
                GameMode.VERSUS_PLAYER.value
            )
        }
        binding.btnVersusComputer.setOnClickListener {
            navigateToGame(GameMode.VERSUS_COMPUTER.value)
        }
        binding.btnOptions.setOnClickListener {
            showOptionsDialog()
        }
        binding.btnExitGame.setOnClickListener {
            finishAndRemoveTask()
        }
    }

    private fun navigateToGame(gameMode: Int) {
        GameActivity.startActivity(this, gameMode)
    }

    private fun getPrefsData() {
        isFirstRunApp = gamePreference.isFirstRunApp
        namePlayerOne = gamePreference.namePlayerOne.orEmpty()
        namePlayerTwo = gamePreference.namePlayerTwo.orEmpty()
    }

    private fun showNotificationDialog() {
        GameMenuDialogFragment(GameMenuDialog.NOTIFICATION) {}.show(
            supportFragmentManager,
            Constants.TAG_NOTIFICATION_DIALOG
        )
    }

    private fun showOptionsDialog() {
        GameMenuDialogFragment(GameMenuDialog.OPTIONS) { message ->
            Utils.showSnackBar(this, binding.root, message.orEmpty())
        }.show(supportFragmentManager, Constants.TAG_OPTIONS_DIALOG)
    }

    companion object {
        @JvmStatic
        fun startActivity(context: Context?) {
            val intent = Intent(context, GameMenuActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            context?.startActivity(intent)
        }
    }
}