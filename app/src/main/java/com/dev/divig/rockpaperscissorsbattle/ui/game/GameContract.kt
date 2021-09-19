package com.dev.divig.rockpaperscissorsbattle.ui.game

import android.content.Context
import com.dev.divig.rockpaperscissorsbattle.data.GameResultEntity

interface GameContract {
    fun getGameResult(
        context: Context,
        valuePlayerOne: Int,
        valuePlayerTwo: Int,
        scorePlayerOne: Int,
        scorePlayerTwo: Int
    ): GameResultEntity
}