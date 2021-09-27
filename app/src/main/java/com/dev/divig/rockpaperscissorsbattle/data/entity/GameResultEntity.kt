package com.dev.divig.rockpaperscissorsbattle.data.entity

import com.dev.divig.rockpaperscissorsbattle.enum.PlayerPosition

data class GameResultEntity(
    val scorePlayerOne: Int,
    val scorePlayerTwo: Int,
    val winnerPosition: PlayerPosition?,
    val winnerColor: Int,
    val resultMessage: String
)
