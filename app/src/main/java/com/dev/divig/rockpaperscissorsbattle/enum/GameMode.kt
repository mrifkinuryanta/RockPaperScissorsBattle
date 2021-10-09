package com.dev.divig.rockpaperscissorsbattle.enum

enum class GameMode(val value: Int) {
    VERSUS_PLAYER(101),
    VERSUS_COMPUTER(102);

    companion object {
        fun state(value: Int) = values().first { it.value == value }
    }
}