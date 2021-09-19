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
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.dev.divig.rockpaperscissorsbattle.R
import com.dev.divig.rockpaperscissorsbattle.databinding.ActivityGameBinding
import com.dev.divig.rockpaperscissorsbattle.databinding.LayoutGameResultDialogBinding
import com.dev.divig.rockpaperscissorsbattle.enum.PlayerActionState
import com.dev.divig.rockpaperscissorsbattle.enum.PlayerPosition
import com.dev.divig.rockpaperscissorsbattle.utils.Constants
import com.dev.divig.rockpaperscissorsbattle.utils.Utils
import com.dev.divig.rockpaperscissorsbattle.utils.Utils.setBackgroundAction
import com.dev.divig.rockpaperscissorsbattle.utils.Utils.setBackgroundColor

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    private val gameController = GameController()
    private var valuePlayerOne: Int = Constants.ZERO
    private var valuePlayerTwo: Int = Constants.ZERO
    private var scorePlayerOne: Int = Constants.ZERO
    private var scorePlayerTwo: Int = Constants.ZERO
    private var winnerColor: Int = Constants.ZERO
    private var isGameFinished: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        setClickAction()
    }

    private fun initView() {
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvRunningText.isSelected = true
    }

    private fun setClickAction() {
        binding.ivRockLeft.setOnClickListener {
            if (!isGameFinished) {
                valuePlayerOne = PlayerActionState.ROCK.value
                setPlayerChoiceBackground(valuePlayerOne, PlayerPosition.LEFT)
                startGame()
            }
        }
        binding.ivPaperLeft.setOnClickListener {
            if (!isGameFinished) {
                valuePlayerOne = PlayerActionState.PAPER.value
                setPlayerChoiceBackground(valuePlayerOne, PlayerPosition.LEFT)
                startGame()
            }
        }
        binding.ivScissorsLeft.setOnClickListener {
            if (!isGameFinished) {
                valuePlayerOne = PlayerActionState.SCISSORS.value
                setPlayerChoiceBackground(valuePlayerOne, PlayerPosition.LEFT)
                startGame()
            }
        }
        binding.ivResetBtn.setOnClickListener {
            Utils.showSnackBar(this, binding.root, getString(R.string.msg_reset_game))
            scorePlayerOne = Constants.ZERO
            scorePlayerTwo = Constants.ZERO
            setScore()
            resetGame()
        }
    }

    private fun startGame() {
        Log.d(Constants.TAG, "First player choice: ${getPlayerChoice(valuePlayerOne)}")
        valuePlayerTwo = (0..2).random()
        Log.d(Constants.TAG, "Second player choice: ${getPlayerChoice(valuePlayerTwo)}")
        setPlayerChoiceBackground(valuePlayerTwo, PlayerPosition.RIGHT)
        showGameResult()
        isGameFinished = true
    }

    private fun getPlayerChoice(value: Int): String {
        return when (PlayerActionState.state(value)) {
            PlayerActionState.ROCK -> getString(R.string.text_rock)
            PlayerActionState.PAPER -> getString(R.string.text_paper)
            PlayerActionState.SCISSORS -> getString(R.string.text_scissors)
        }
    }

    private fun setPlayerChoiceBackground(value: Int, playerPosition: PlayerPosition) {
        val ivRock: ImageView
        val ivPaper: ImageView
        val ivScissors: ImageView
        when (playerPosition) {
            PlayerPosition.LEFT -> {
                ivRock = binding.ivRockLeft
                ivPaper = binding.ivPaperLeft
                ivScissors = binding.ivScissorsLeft
            }
            PlayerPosition.RIGHT -> {
                ivRock = binding.ivRockRight
                ivPaper = binding.ivPaperRight
                ivScissors = binding.ivScissorsRight
            }
        }

        when (PlayerActionState.state(value)) {
            PlayerActionState.ROCK -> ivRock.setBackgroundAction(this)
            PlayerActionState.PAPER -> ivPaper.setBackgroundAction(this)
            PlayerActionState.SCISSORS -> ivScissors.setBackgroundAction(this)
        }
    }

    private fun showGameResult() {
        val gameResult = gameController.getGameResult(
            this,
            valuePlayerOne,
            valuePlayerTwo,
            scorePlayerOne,
            scorePlayerTwo
        )
        winnerColor = gameResult.winnerColor
        scorePlayerOne = gameResult.scorePlayerOne
        scorePlayerTwo = gameResult.scorePlayerTwo
        setScore()

        val resultMessage = gameResult.resultMessage
        showGameResultDialog(resultMessage)
        Log.d(Constants.TAG, "Result Game: $resultMessage")
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

    private fun showGameResultDialog(resultMessage: String) {
        binding.tvPlaceholderVersus.visibility = View.INVISIBLE
        val dialogBinding = LayoutGameResultDialogBinding.inflate(layoutInflater)
        val gameResultDialog = Dialog(this)
        gameResultDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        gameResultDialog.setContentView(dialogBinding.root)
        gameResultDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        gameResultDialog.setCancelable(false)
        gameResultDialog.setCanceledOnTouchOutside(false)

        dialogBinding.tvGameResult.text = resultMessage
        dialogBinding.tvGameResult.setBackgroundColor(this, winnerColor)
        dialogBinding.pbTimer.max = Constants.THREE

        object : CountDownTimer(Constants.FOUR_SECOND, Constants.ONE_SECOND) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = (millisUntilFinished / Constants.ONE_SECOND).toInt()
                if (seconds == Constants.THREE) gameResultDialog.show()

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    dialogBinding.pbTimer.setProgress(seconds, true)
                } else {
                    dialogBinding.pbTimer.progress = seconds
                }
                dialogBinding.tvTimer.text = seconds.toString()
            }

            override fun onFinish() {
                resetGame()
                gameResultDialog.dismiss()
                binding.tvPlaceholderVersus.visibility = View.VISIBLE
            }
        }.start()
    }
}