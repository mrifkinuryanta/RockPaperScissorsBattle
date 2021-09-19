package com.dev.divig.rockpaperscissorsbattle.enum

enum class PlayerActionState(val value: Int) {
    ROCK(0),
    PAPER(1),
    SCISSORS(2);

    companion object {
        fun state(value: Int) = values().first { it.value == value }
    }
}