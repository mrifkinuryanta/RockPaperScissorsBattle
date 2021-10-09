package com.dev.divig.rockpaperscissorsbattle.ui.menu

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.dev.divig.rockpaperscissorsbattle.R
import com.dev.divig.rockpaperscissorsbattle.data.preference.GamePreference
import com.dev.divig.rockpaperscissorsbattle.databinding.LayoutGameMenuDialogBinding
import com.dev.divig.rockpaperscissorsbattle.enum.GameMenuDialog
import com.dev.divig.rockpaperscissorsbattle.enum.GameMode
import com.dev.divig.rockpaperscissorsbattle.ui.game.GameActivity
import com.dev.divig.rockpaperscissorsbattle.utils.Constants
import com.dev.divig.rockpaperscissorsbattle.utils.Utils

class GameMenuDialogFragment(
    private val gameMenuDialog: GameMenuDialog,
    private val onClickOkayDialog: (message: String?) -> Unit,
) : DialogFragment() {
    private lateinit var gamePreference: GamePreference

    private var _binding: LayoutGameMenuDialogBinding? = null
    private val binding get() = _binding
    private var isFirstRunApp: Boolean = true
    private var namePlayerOne: String = Constants.EMPTY_VALUE
    private var namePlayerTwo: String = Constants.EMPTY_VALUE

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LayoutGameMenuDialogBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)

        getPrefsData()

        when (gameMenuDialog) {
            GameMenuDialog.NOTIFICATION -> showNotificationDialog()
            GameMenuDialog.OPTIONS -> showOptionsDialog(false)
        }
    }

    private fun getPrefsData() {
        gamePreference = GamePreference(requireContext())
        isFirstRunApp = gamePreference.isFirstRunApp
        namePlayerOne = gamePreference.namePlayerOne.orEmpty()
        namePlayerTwo = gamePreference.namePlayerTwo.orEmpty()
    }

    private fun showNotificationDialog() {
        binding?.groupNotificationLayout?.isVisible = true
        gamePreference.isFirstRunApp = false

        binding?.btnActionOkay?.setOnClickListener {
            binding?.groupNotificationLayout?.isVisible = false
            showOptionsDialog(isFirstRunApp)
        }

        binding?.btnActionCancel?.setOnClickListener {
            dialog?.dismiss()
            navigateToGame()
        }
    }

    private fun showOptionsDialog(isFirstRunApp: Boolean) {
        binding?.groupOptionsLayout?.isVisible = true
        if (isFirstRunApp) {
            binding?.namePlayerOneLayout?.isVisible = false
        }

        getPrefsData()
        binding?.etNamePlayerOne?.setText(namePlayerOne)
        binding?.etNamePlayerTwo?.setText(namePlayerTwo)

        binding?.btnActionOkay?.setOnClickListener {
            Utils.hideSoftKeyboard(requireActivity(), it)

            val etNamePlayerOne = binding?.etNamePlayerOne?.text.toString().trim()
            val etNamePlayerTwo = binding?.etNamePlayerTwo?.text.toString().trim()

            if (etNamePlayerOne != namePlayerOne || etNamePlayerTwo != namePlayerTwo) {
                gamePreference.namePlayerOne = etNamePlayerOne
                gamePreference.namePlayerTwo = etNamePlayerTwo
                dialog?.dismiss()
                onClickOkayDialog(getString(R.string.msg_update_success))
            } else {
                dialog?.dismiss()
                onClickOkayDialog(getString(R.string.msg_no_changes))
            }

            if (isFirstRunApp) {
                navigateToGame()
            }
        }

        binding?.btnActionCancel?.setOnClickListener {
            dialog?.dismiss()
            if (isFirstRunApp) {
                navigateToGame()
            }
        }
    }

    private fun navigateToGame() {
        GameActivity.startActivity(requireContext(), GameMode.VERSUS_PLAYER.value)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}