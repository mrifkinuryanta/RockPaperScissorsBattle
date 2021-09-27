package com.dev.divig.rockpaperscissorsbattle.ui.menu

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.dev.divig.rockpaperscissorsbattle.R
import com.dev.divig.rockpaperscissorsbattle.data.preference.GamePreference
import com.dev.divig.rockpaperscissorsbattle.databinding.ActivityGameMenuBinding
import com.dev.divig.rockpaperscissorsbattle.databinding.LayoutAlertDialogBeforeStartingBinding
import com.dev.divig.rockpaperscissorsbattle.databinding.LayoutOptionsDialogBinding
import com.dev.divig.rockpaperscissorsbattle.enum.GameMode
import com.dev.divig.rockpaperscissorsbattle.ui.game.GameActivity
import com.dev.divig.rockpaperscissorsbattle.utils.Constants
import com.dev.divig.rockpaperscissorsbattle.utils.Utils
import com.dev.divig.rockpaperscissorsbattle.utils.Utils.setVisibilityGone

class GameMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameMenuBinding
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

        binding.ivGameLogo.load(Constants.URL_LOGO) {
            placeholder(R.drawable.img_placeholder)
            error(R.drawable.img_placeholder)
        }

        getPrefsValue()

        val wrd = getString(R.string.msg_welcome, GamePreference(this).namePlayerOne)
        Utils.showSnackBar(this, binding.root, wrd)
    }

    private fun setClickAction() {
        binding.btnVersusPlayer.setOnClickListener {
            getPrefsValue()
            if (isFirstRunApp && namePlayerTwo.isEmpty()) showAlertDialog() else navigateToGame(
                GameMode.VERSUS_PLAYER.value
            )
        }
        binding.btnVersusComputer.setOnClickListener {
            navigateToGame(GameMode.VERSUS_COMPUTER.value)
        }
        binding.btnOptions.setOnClickListener {
            showOptionsDialog(false)
        }
        binding.btnExitGame.setOnClickListener {
            finishAndRemoveTask()
        }
    }

    private fun navigateToGame(gameMode: Int) {
        val intent = Intent(this, GameActivity::class.java).apply {
            putExtra(Constants.EXTRA_VALUE_MODE, gameMode)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        startActivity(intent)
        finish()
    }

    private fun getPrefsValue() {
        isFirstRunApp = GamePreference(this).isFirstRunApp
        namePlayerOne = GamePreference(this).namePlayerOne.orEmpty()
        namePlayerTwo = GamePreference(this).namePlayerTwo.orEmpty()
    }

    private fun showOptionsDialog(isFirstRunApp: Boolean) {
        val dialogBinding = LayoutOptionsDialogBinding.inflate(layoutInflater)
        val optionsDialog = Dialog(this)
        optionsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        optionsDialog.setContentView(dialogBinding.root)
        optionsDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        optionsDialog.setCancelable(false)
        optionsDialog.setCanceledOnTouchOutside(false)

        if (isFirstRunApp) {
            dialogBinding.tilNamePlayerOne.setVisibilityGone(false)
        }

        getPrefsValue()
        dialogBinding.etNamePlayerOne.setText(namePlayerOne)
        dialogBinding.etNamePlayerTwo.setText(namePlayerTwo)

        dialogBinding.btnActionOkay.setOnClickListener {
            val imm =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)

            val etNamePlayerOne = dialogBinding.etNamePlayerOne.text.toString().trim()
            val etNamePlayerTwo = dialogBinding.etNamePlayerTwo.text.toString().trim()

            if (etNamePlayerOne != namePlayerOne || etNamePlayerTwo != namePlayerTwo) {
                GamePreference(this).namePlayerOne = etNamePlayerOne
                GamePreference(this).namePlayerTwo = etNamePlayerTwo
                optionsDialog.dismiss()
                Utils.showSnackBar(this, binding.root, getString(R.string.msg_update_success))
            } else {
                optionsDialog.dismiss()
                Utils.showSnackBar(this, binding.root, getString(R.string.msg_no_changes))
            }

            if (isFirstRunApp) {
                navigateToGame(GameMode.VERSUS_PLAYER.value)
            }
        }

        dialogBinding.btnActionCancel.setOnClickListener {
            optionsDialog.dismiss()
            if (isFirstRunApp) {
                navigateToGame(GameMode.VERSUS_PLAYER.value)
            }
        }
        optionsDialog.show()
    }

    private fun showAlertDialog() {
        GamePreference(this).isFirstRunApp = false

        val dialogBinding = LayoutAlertDialogBeforeStartingBinding.inflate(layoutInflater)
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        dialogBinding.btnActionOkay.setOnClickListener {
            dialog.dismiss()
            showOptionsDialog(isFirstRunApp)
        }

        dialogBinding.btnActionCancel.setOnClickListener {
            dialog.dismiss()
            navigateToGame(GameMode.VERSUS_PLAYER.value)
        }
        dialog.show()
    }
}