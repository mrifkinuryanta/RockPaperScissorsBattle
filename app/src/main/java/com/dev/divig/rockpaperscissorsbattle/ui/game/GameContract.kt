package com.dev.divig.rockpaperscissorsbattle.ui.game

import android.content.Context
import com.dev.divig.rockpaperscissorsbattle.data.entity.GameResultEntity
import com.dev.divig.rockpaperscissorsbattle.data.entity.ScoringEntity

interface GameContract {
    fun getGameResult(context: Context, scoringEntity: ScoringEntity): GameResultEntity
}