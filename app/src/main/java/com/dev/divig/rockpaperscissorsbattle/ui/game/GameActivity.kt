package com.dev.divig.rockpaperscissorsbattle.ui.game

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.dev.divig.rockpaperscissorsbattle.R
import com.dev.divig.rockpaperscissorsbattle.databinding.ActivityGameBinding
import com.dev.divig.rockpaperscissorsbattle.enum.ActionState
import com.dev.divig.rockpaperscissorsbattle.utils.Utils
import com.dev.divig.rockpaperscissorsbattle.utils.Utils.setBackground

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding

    private var scorePlayerOne: Int = 0
    private var scorePlayerTwo: Int = 0
    private var isGameFinished: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.ivBackground.load(R.drawable.background)
        setClickAction()
    }

    private fun setClickAction() {
        binding.ivRock1.setOnClickListener {
            if (!isGameFinished) {
                binding.ivRock1.setBackground(this)
                startGame(ActionState.ROCK.value)
            }
        }
        binding.ivPaper1.setOnClickListener {
            if (!isGameFinished) {
                binding.ivPaper1.setBackground(this)
                startGame(ActionState.PAPER.value)
            }
        }
        binding.ivScissors1.setOnClickListener {
            if (!isGameFinished) {
                binding.ivScissors1.setBackground(this)
                startGame(ActionState.SCISSORS.value)
            }
        }
        binding.ivReset.setOnClickListener {
            Utils.showSnackBar(this, binding.root, "Reset")
            resetGame()
        }

        binding.ivReset.setOnLongClickListener {
            Utils.showSnackBar(this, binding.root, "Reset Score")
            scorePlayerOne = 0
            scorePlayerTwo = 0
            setScore()
            resetGame()
            true
        }
    }

    private fun startGame(valuePlayerOne: Int) {
        val valuePlayerTwo = (0..2).random()
        playerTwoChoice(valuePlayerTwo)
        Utils.showSnackBar(this, binding.root, resultGame(valuePlayerOne, valuePlayerTwo))
        setScore()
        isGameFinished = true
    }

    private fun playerTwoChoice(value: Int) {
        when (value) {
            ActionState.ROCK.value -> binding.ivRock2.setBackground(this)
            ActionState.PAPER.value -> binding.ivPaper2.setBackground(this)
            ActionState.SCISSORS.value -> binding.ivScissors2.setBackground(this)
        }
    }

    private fun resultGame(p1: Int, p2: Int): String {
        return when {
            (p1 + 1) % 3 == p2 -> {
                scorePlayerTwo++
                "Player 2 won"
            }
            p1 == p2 -> {
                "It is a draw"
            }
            else -> {
                scorePlayerOne++
                "Player 1 won"
            }
        }
    }

    private fun setScore() {
        binding.tvScorePlayerOne.text = scorePlayerOne.toString()
        binding.tvScorePlayerTwo.text = scorePlayerTwo.toString()
    }

    private fun resetGame() {
        binding.ivRock1.background = null
        binding.ivPaper1.background = null
        binding.ivScissors1.background = null
        binding.ivRock2.background = null
        binding.ivPaper2.background = null
        binding.ivScissors2.background = null
        isGameFinished = false
    }
}