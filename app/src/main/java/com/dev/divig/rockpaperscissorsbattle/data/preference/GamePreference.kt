package com.dev.divig.rockpaperscissorsbattle.data.preference

import android.content.Context
import android.content.SharedPreferences
import com.dev.divig.rockpaperscissorsbattle.utils.Constants

class GamePreference(context: Context) {
    private var preference = context.getSharedPreferences(Constants.PREF_NAME, Constants.PREF_MODE)

    var isFirstRunApp: Boolean
        get() = preference.getBoolean(PREF_IS_FIRST_RUN_APP.first, PREF_IS_FIRST_RUN_APP.second)
        set(value) = preference.edit {
            it.putBoolean(PREF_IS_FIRST_RUN_APP.first, value)
        }

    var namePlayerOne: String?
        get() = preference.getString(PREF_NAME_PLAYER_ONE.first, PREF_NAME_PLAYER_ONE.second)
        set(value) = preference.edit {
            it.putString(PREF_NAME_PLAYER_ONE.first, value)
        }

    var namePlayerTwo: String?
        get() = preference.getString(PREF_NAME_PLAYER_TWO.first, PREF_NAME_PLAYER_TWO.second)
        set(value) = preference.edit {
            it.putString(PREF_NAME_PLAYER_TWO.first, value)
        }

    companion object {
        private val PREF_IS_FIRST_RUN_APP = Pair("IS_FIRST_RUN_APP", true)
        private val PREF_NAME_PLAYER_ONE = Pair("NAME_PLAYER_ONE", Constants.EMPTY_VALUE)
        private val PREF_NAME_PLAYER_TWO = Pair("NAME_PLAYER_TWO", Constants.EMPTY_VALUE)
    }
}

private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
    val editor = edit()
    operation(editor)
    editor.apply()
}