package com.dev.divig.rockpaperscissorsbattle.ui.game

import android.content.Context
import com.dev.divig.rockpaperscissorsbattle.data.entity.GameResultEntity

interface GameContract {
    fun getGameResult(
        context: Context,
        gameMode: Int,
        valuePlayerOne: Int,
        valuePlayerTwo: Int,
        scorePlayerOne: Int,
        scorePlayerTwo: Int
    ): GameResultEntity
}