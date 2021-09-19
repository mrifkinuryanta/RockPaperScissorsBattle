package com.dev.divig.rockpaperscissorsbattle.data

data class GameResultEntity(
    val scorePlayerOne: Int,
    val scorePlayerTwo: Int,
    val winnerColor: Int,
    val resultMessage: String
)
