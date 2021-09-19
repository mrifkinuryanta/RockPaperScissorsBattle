package com.dev.divig.rockpaperscissorsbattle.ui.game

import android.content.Context
import com.dev.divig.rockpaperscissorsbattle.R
import com.dev.divig.rockpaperscissorsbattle.data.GameResultEntity
import com.dev.divig.rockpaperscissorsbattle.enum.WinnerColor
import com.dev.divig.rockpaperscissorsbattle.utils.Constants

class GameController : GameContract {
    override fun getGameResult(
        context: Context,
        valuePlayerOne: Int,
        valuePlayerTwo: Int,
        scorePlayerOne: Int,
        scorePlayerTwo: Int
    ): GameResultEntity {
        return when {
            (valuePlayerOne + Constants.ONE) % Constants.THREE == valuePlayerTwo -> {
                GameResultEntity(
                    scorePlayerOne,
                    scorePlayerTwo + Constants.ONE,
                    WinnerColor.RED.color,
                    context.getString(R.string.msg_lose)
                )
            }
            valuePlayerOne == valuePlayerTwo -> {
                GameResultEntity(
                    scorePlayerOne,
                    scorePlayerTwo,
                    WinnerColor.BLUE.color,
                    context.getString(R.string.msg_draw)
                )
            }
            else -> {
                GameResultEntity(
                    scorePlayerOne + Constants.ONE,
                    scorePlayerTwo,
                    WinnerColor.GREEN.color,
                    context.getString(R.string.msg_win)
                )
            }
        }
    }
}