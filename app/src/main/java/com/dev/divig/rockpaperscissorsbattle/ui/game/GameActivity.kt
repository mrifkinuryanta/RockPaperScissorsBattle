package com.dev.divig.rockpaperscissorsbattle.ui.game

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.dev.divig.rockpaperscissorsbattle.R
import com.dev.divig.rockpaperscissorsbattle.databinding.ActivityGameBinding
import com.dev.divig.rockpaperscissorsbattle.databinding.LayoutResultGameDialogBinding
import com.dev.divig.rockpaperscissorsbattle.enum.ActionState
import com.dev.divig.rockpaperscissorsbattle.enum.WinnerColor
import com.dev.divig.rockpaperscissorsbattle.utils.Constant
import com.dev.divig.rockpaperscissorsbattle.utils.Utils
import com.dev.divig.rockpaperscissorsbattle.utils.Utils.setBackgroundBrown
import com.dev.divig.rockpaperscissorsbattle.utils.Utils.setBackgroundColor

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    private var scorePlayerOne: Int = Constant.ZERO
    private var scorePlayerTwo: Int = Constant.ZERO
    private var winnerColor: Int = Constant.ZERO
    private var isGameFinished: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        setClickActionAndStartGame()
    }

    private fun initView() {
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvRunningText.isSelected = true
    }

    private fun setClickActionAndStartGame() {
        binding.ivRockLeft.setOnClickListener {
            if (!isGameFinished) {
                it.setBackgroundBrown(this)
                startGame(ActionState.ROCK.value)
            }
        }
        binding.ivPaperLeft.setOnClickListener {
            if (!isGameFinished) {
                it.setBackgroundBrown(this)
                startGame(ActionState.PAPER.value)
            }
        }
        binding.ivScissorsLeft.setOnClickListener {
            if (!isGameFinished) {
                it.setBackgroundBrown(this)
                startGame(ActionState.SCISSORS.value)
            }
        }
        binding.ivResetBtn.setOnClickListener {
            Utils.showSnackBar(this, binding.root, getString(R.string.msg_reset_game))
            scorePlayerOne = Constant.ZERO
            scorePlayerTwo = Constant.ZERO
            setScore()
        }
    }

    private fun startGame(valuePlayerOne: Int) {
        Log.d(Constant.TAG, "First player choice: ${playerChoice(valuePlayerOne)}")
        val valuePlayerTwo = (0..2).random()
        Log.d(Constant.TAG, "Second player choice: ${playerChoice(valuePlayerTwo)}")
        setPlayerTwoBackground(valuePlayerTwo)
        val resultGame = resultGame(valuePlayerOne, valuePlayerTwo)
        resultGameDialog(resultGame)
        Log.d(Constant.TAG, "Result Game: $resultGame")
        setScore()
        isGameFinished = true
    }

    private fun playerChoice(value: Int): String {
        return when (value) {
            ActionState.ROCK.value -> getString(R.string.text_rock)
            ActionState.PAPER.value -> getString(R.string.text_paper)
            ActionState.SCISSORS.value -> getString(R.string.text_scissors)
            else -> Constant.EMPTY_VALUE
        }
    }

    private fun setPlayerTwoBackground(value: Int) {
        when (value) {
            ActionState.ROCK.value -> binding.ivRockRight.setBackgroundBrown(this)
            ActionState.PAPER.value -> binding.ivPaperRight.setBackgroundBrown(this)
            ActionState.SCISSORS.value -> binding.ivScissorsRight.setBackgroundBrown(this)
        }
    }

    private fun resultGame(p1: Int, p2: Int): String {
        return when {
            (p1 + Constant.ONE) % Constant.THREE == p2 -> {
                scorePlayerTwo++
                winnerColor = WinnerColor.RED.color
                getString(R.string.msg_lose)
            }
            p1 == p2 -> {
                winnerColor = WinnerColor.BLUE.color
                getString(R.string.msg_draw)
            }
            else -> {
                scorePlayerOne++
                winnerColor = WinnerColor.GREEN.color
                getString(R.string.msg_win)
            }
        }
    }

    private fun setScore() {
        binding.tvScorePlayerOne.text = scorePlayerOne.toString()
        binding.tvScorePlayerTwo.text = scorePlayerTwo.toString()
    }

    private fun resetGame() {
        binding.ivRockLeft.background = null
        binding.ivPaperLeft.background = null
        binding.ivScissorsLeft.background = null
        binding.ivRockRight.background = null
        binding.ivPaperRight.background = null
        binding.ivScissorsRight.background = null
        isGameFinished = false
    }

    private fun resultGameDialog(resultGame: String) {
        binding.tvPlaceholderVersus.visibility = View.INVISIBLE
        val dialogBinding = LayoutResultGameDialogBinding.inflate(layoutInflater)
        val resultGameDialog = Dialog(this)
        resultGameDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        resultGameDialog.setContentView(dialogBinding.root)
        resultGameDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        resultGameDialog.setCancelable(false)
        resultGameDialog.setCanceledOnTouchOutside(false)

        dialogBinding.tvResultGame.text = resultGame
        dialogBinding.tvResultGame.setBackgroundColor(this, winnerColor)
        dialogBinding.pbTimer.max = Constant.THREE

        object : CountDownTimer(Constant.FOUR_SECOND, Constant.ONE_SECOND) {
            override fun onTick(millisUntilFinished: Long) {
                if ((millisUntilFinished / Constant.ONE_SECOND).toInt() == Constant.THREE) resultGameDialog.show()

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    dialogBinding.pbTimer.setProgress(
                        (millisUntilFinished / Constant.ONE_SECOND).toInt(),
                        true
                    )
                } else {
                    dialogBinding.pbTimer.progress =
                        (millisUntilFinished / Constant.ONE_SECOND).toInt()
                }
                dialogBinding.tvTimer.text = (millisUntilFinished / Constant.ONE_SECOND).toString()
            }

            override fun onFinish() {
                resetGame()
                resultGameDialog.dismiss()
                binding.tvPlaceholderVersus.visibility = View.VISIBLE
            }
        }.start()
    }
}