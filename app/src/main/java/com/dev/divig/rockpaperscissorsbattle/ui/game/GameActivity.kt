package com.dev.divig.rockpaperscissorsbattle.ui.game

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Window
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.dev.divig.rockpaperscissorsbattle.R
import com.dev.divig.rockpaperscissorsbattle.data.entity.ScoringEntity
import com.dev.divig.rockpaperscissorsbattle.data.preference.GamePreference
import com.dev.divig.rockpaperscissorsbattle.databinding.ActivityGameBinding
import com.dev.divig.rockpaperscissorsbattle.databinding.LayoutGameResultDialogBinding
import com.dev.divig.rockpaperscissorsbattle.enum.GameMode
import com.dev.divig.rockpaperscissorsbattle.enum.PlayerActionState
import com.dev.divig.rockpaperscissorsbattle.enum.PlayerPosition
import com.dev.divig.rockpaperscissorsbattle.ui.menu.GameMenuActivity
import com.dev.divig.rockpaperscissorsbattle.utils.Constants
import com.dev.divig.rockpaperscissorsbattle.utils.Utils
import com.dev.divig.rockpaperscissorsbattle.utils.Utils.setBackgroundAction
import com.dev.divig.rockpaperscissorsbattle.utils.Utils.setBackgroundTextColor

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    private val gameController = GameController()
    private var namePlayerOne: String = Constants.EMPTY_VALUE
    private var namePlayerTwo: String = Constants.EMPTY_VALUE
    private var valuePlayerOne: Int = Constants.ZERO
    private var valuePlayerTwo: Int = Constants.ZERO
    private var scorePlayerOne: Int = Constants.ZERO
    private var scorePlayerTwo: Int = Constants.ZERO

    private var winnerColor: Int = Constants.ZERO
    private var isGameFinished: Boolean = false
    private var gameMode: Int = Constants.ZERO
    private var playerTurn: PlayerPosition = PlayerPosition.LEFT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setInitialState()
        setClickAction()
    }

    private fun init() {
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvRunningText.isSelected = true
        binding.tvNamePlayerLeft.isSelected = true
        binding.tvNamePlayerRight.isSelected = true

        val extras = intent.extras
        if (extras != null) {
            gameMode = extras.getInt(Constants.EXTRAS_VALUE_MODE)
        }

        val gamePreference = GamePreference(this)
        namePlayerOne = gamePreference.namePlayerOne.orEmpty()
        namePlayerTwo = gamePreference.namePlayerTwo.orEmpty()

        namePlayerOne =
            if (namePlayerOne.isNotEmpty()) {
                namePlayerOne
            } else {
                getString(R.string.text_placeholder_name_player_one)
            }
        namePlayerTwo =
            if (gameMode == GameMode.VERSUS_PLAYER.value) {
                when {
                    namePlayerTwo.isNotEmpty() -> namePlayerTwo
                    else -> getString(R.string.text_placeholder_name_player_two)
                }
            } else {
                getString(R.string.text_placeholder_name_player_com)
            }

        binding.tvNamePlayerLeft.text = namePlayerOne
        binding.tvNamePlayerRight.text = namePlayerTwo
    }

    private fun setInitialState() {
        resetGame()
        if (gameMode == GameMode.VERSUS_PLAYER.value) {
            setPlayerTurn(PlayerPosition.LEFT)
        }
    }

    private fun setClickAction() {
        binding.ivRockLeft.setOnClickListener {
            if (!isGameFinished) {
                valuePlayerOne = PlayerActionState.ROCK.value
                Utils.showToast(
                    this,
                    getPlayerChoiceMessage(valuePlayerOne, PlayerPosition.LEFT)
                )
                setNextTurn()
            }
        }
        binding.ivPaperLeft.setOnClickListener {
            if (!isGameFinished) {
                valuePlayerOne = PlayerActionState.PAPER.value
                Utils.showToast(
                    this,
                    getPlayerChoiceMessage(valuePlayerOne, PlayerPosition.LEFT)
                )
                setNextTurn()
            }
        }
        binding.ivScissorsLeft.setOnClickListener {
            if (!isGameFinished) {
                valuePlayerOne = PlayerActionState.SCISSORS.value
                Utils.showToast(
                    this,
                    getPlayerChoiceMessage(valuePlayerOne, PlayerPosition.LEFT)
                )
                setNextTurn()
            }
        }
        binding.ivRockRight.setOnClickListener {
            if (!isGameFinished && playerTurn == PlayerPosition.RIGHT) {
                valuePlayerTwo = PlayerActionState.ROCK.value
                startGame()
            }
        }
        binding.ivPaperRight.setOnClickListener {
            if (!isGameFinished && playerTurn == PlayerPosition.RIGHT) {
                valuePlayerTwo = PlayerActionState.PAPER.value
                startGame()
            }
        }
        binding.ivScissorsRight.setOnClickListener {
            if (!isGameFinished && playerTurn == PlayerPosition.RIGHT) {
                valuePlayerTwo = PlayerActionState.SCISSORS.value
                startGame()
            }
        }
        binding.ivResetBtn.setOnClickListener {
            Utils.showSnackBar(this, binding.root, getString(R.string.msg_reset_score))
            scorePlayerOne = Constants.ZERO
            scorePlayerTwo = Constants.ZERO
            setScore()
            setInitialState()
        }
        binding.ivExitBtn.setOnClickListener {
            navigateToGameMenu()
        }
    }

    private fun startGame() {
        when (GameMode.state(gameMode)) {
            GameMode.VERSUS_PLAYER -> {
                showActionPlayer(PlayerPosition.LEFT, true)
                showActionPlayer(PlayerPosition.RIGHT, true)
            }
            GameMode.VERSUS_COMPUTER -> {
                valuePlayerTwo = (0..2).random()
            }
        }
        Log.d(
            Constants.TAG,
            getPlayerChoiceMessage(valuePlayerOne, PlayerPosition.LEFT)
        )
        setPlayerChoiceBackground(valuePlayerOne, PlayerPosition.LEFT)

        val msgPlayerTwo = getPlayerChoiceMessage(valuePlayerTwo, PlayerPosition.RIGHT)
        Utils.showToast(this, msgPlayerTwo)
        Log.d(Constants.TAG, msgPlayerTwo)
        setPlayerChoiceBackground(valuePlayerTwo, PlayerPosition.RIGHT)

        showGameResult()
        isGameFinished = true
    }

    private fun getPlayerChoiceMessage(value: Int, playerPosition: PlayerPosition): String {
        val playerChoice = when (PlayerActionState.state(value)) {
            PlayerActionState.ROCK -> getString(R.string.text_rock)
            PlayerActionState.PAPER -> getString(R.string.text_paper)
            PlayerActionState.SCISSORS -> getString(R.string.text_scissors)
        }

        return when (playerPosition) {
            PlayerPosition.LEFT -> getString(
                R.string.msg_first_player_choice,
                playerChoice
            )
            PlayerPosition.RIGHT -> getString(
                R.string.msg_second_player_choice,
                playerChoice
            )
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
        val scoringEntity = ScoringEntity(
            gameMode,
            valuePlayerOne,
            valuePlayerTwo,
            scorePlayerOne,
            scorePlayerTwo
        )

        val gameResult = gameController.getGameResult(this, scoringEntity)
        winnerColor = gameResult.winnerColor
        scorePlayerOne = gameResult.scorePlayerOne
        scorePlayerTwo = gameResult.scorePlayerTwo
        setScore()

        val winnerPosition = gameResult.winnerPosition
        var resultMessage = gameResult.resultMessage
        if (gameMode == GameMode.VERSUS_PLAYER.value) {
            resultMessage = when (winnerPosition) {
                PlayerPosition.LEFT -> "$namePlayerOne \n $resultMessage"
                PlayerPosition.RIGHT -> "$namePlayerTwo \n $resultMessage"
                else -> resultMessage
            }
        }
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

    private fun navigateToGameMenu() {
        GameMenuActivity.startActivity(this)
    }

    private fun setNextTurn() {
        when (GameMode.state(gameMode)) {
            GameMode.VERSUS_PLAYER -> setPlayerTurn(PlayerPosition.RIGHT)
            GameMode.VERSUS_COMPUTER -> startGame()
        }
    }

    private fun setPlayerTurn(playerPosition: PlayerPosition) {
        playerTurn = when (playerPosition) {
            PlayerPosition.LEFT -> {
                showActionPlayer(PlayerPosition.LEFT, true)
                showActionPlayer(PlayerPosition.RIGHT, false)
                PlayerPosition.LEFT
            }
            PlayerPosition.RIGHT -> {
                showActionPlayer(PlayerPosition.LEFT, false)
                showActionPlayer(PlayerPosition.RIGHT, true)
                PlayerPosition.RIGHT
            }
        }
    }

    private fun showActionPlayer(playerPosition: PlayerPosition, isVisible: Boolean) {
        when (playerPosition) {
            PlayerPosition.LEFT -> {
                binding.llActionLeft.isVisible = isVisible
            }
            PlayerPosition.RIGHT -> {
                binding.llActionRight.isVisible = isVisible
            }
        }
    }

    private fun showGameResultDialog(resultMessage: String) {
        binding.tvPlaceholderVersus.isInvisible
        val dialogBinding = LayoutGameResultDialogBinding.inflate(layoutInflater)
        val gameResultDialog = Dialog(this)
        gameResultDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        gameResultDialog.setContentView(dialogBinding.root)
        gameResultDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        gameResultDialog.setCancelable(false)
        gameResultDialog.setCanceledOnTouchOutside(false)

        dialogBinding.tvGameResult.text = resultMessage
        dialogBinding.tvGameResult.setBackgroundTextColor(this, winnerColor)
        dialogBinding.pbTimer.max = Constants.THREE

        dialogBinding.btnActionPlayAgain.setOnClickListener {
            gameResultDialog.dismiss()
            setInitialState()
            binding.tvPlaceholderVersus.isVisible
        }

        dialogBinding.btnActionMainMenu.setOnClickListener {
            gameResultDialog.dismiss()
            navigateToGameMenu()
        }

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
                dialogBinding.groupTimerLayout.isInvisible = true
                dialogBinding.groupActionLayout.isInvisible = false
            }
        }.start()
    }

    companion object {
        @JvmStatic
        fun startActivity(context: Context?, gameMode: Int) {
            val intent = Intent(context, GameActivity::class.java).apply {
                putExtra(Constants.EXTRAS_VALUE_MODE, gameMode)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            context?.startActivity(intent)
        }
    }
}