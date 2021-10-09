package com.dev.divig.rockpaperscissorsbattle.ui.game

import android.content.Context
import com.dev.divig.rockpaperscissorsbattle.R
import com.dev.divig.rockpaperscissorsbattle.data.entity.GameResultEntity
import com.dev.divig.rockpaperscissorsbattle.data.entity.ScoringEntity
import com.dev.divig.rockpaperscissorsbattle.enum.GameMode
import com.dev.divig.rockpaperscissorsbattle.enum.PlayerPosition
import com.dev.divig.rockpaperscissorsbattle.enum.WinnerColor
import com.dev.divig.rockpaperscissorsbattle.utils.Constants

class GameController : GameContract {
    override fun getGameResult(context: Context, scoringEntity: ScoringEntity): GameResultEntity {
        return with(scoringEntity) {
            when {
                (valuePlayerOne + Constants.ONE) % Constants.THREE == valuePlayerTwo -> {
                    val winnerColor = when (GameMode.state(gameMode)) {
                        GameMode.VERSUS_PLAYER -> WinnerColor.GREEN.color
                        GameMode.VERSUS_COMPUTER -> WinnerColor.RED.color
                    }
                    val msg = when (GameMode.state(gameMode)) {
                        GameMode.VERSUS_PLAYER -> context.getString(R.string.msg_win)
                        GameMode.VERSUS_COMPUTER -> context.getString(R.string.msg_lose)
                    }
                    GameResultEntity(
                        scorePlayerOne,
                        scorePlayerTwo + Constants.ONE,
                        PlayerPosition.RIGHT,
                        winnerColor,
                        msg
                    )
                }
                valuePlayerOne == valuePlayerTwo -> {
                    GameResultEntity(
                        scorePlayerOne,
                        scorePlayerTwo,
                        null,
                        WinnerColor.BLUE.color,
                        context.getString(R.string.msg_draw)
                    )
                }
                else -> {
                    GameResultEntity(
                        scorePlayerOne + Constants.ONE,
                        scorePlayerTwo,
                        PlayerPosition.LEFT,
                        WinnerColor.GREEN.color,
                        context.getString(R.string.msg_win)
                    )
                }
            }
        }
    }
}