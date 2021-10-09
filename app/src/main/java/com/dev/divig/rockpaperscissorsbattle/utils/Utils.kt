package com.dev.divig.rockpaperscissorsbattle.utils

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.dev.divig.rockpaperscissorsbattle.R
import com.dev.divig.rockpaperscissorsbattle.enum.WinnerColor
import com.google.android.material.snackbar.Snackbar

object Utils {
    fun showToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun showSnackBar(context: Context, view: View, msg: String) {
        snackBar(context, view, msg, R.drawable.bg_corners_brown)
    }

    private fun snackBar(context: Context, view: View, msg: String, backgroundDrawable: Int) {
        val snackBar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
        val snackView = snackBar.view
        val tv = snackView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tv.setTextAppearance(R.style.Text_Game_App)
        }
        tv.textSize = Constants.SIXTEEN_FLOAT
        tv.setTextColor(ContextCompat.getColor(context, R.color.white))
        snackBar.view.background = ContextCompat.getDrawable(context, backgroundDrawable)
        snackBar.show()
    }

    fun View.setBackgroundAction(context: Context) {
        background = ContextCompat.getDrawable(context, R.drawable.bg_corners_brown)
    }

    fun View.setBackgroundTextColor(context: Context, color: Int) {
        when (color) {
            WinnerColor.GREEN.color -> background =
                ContextCompat.getDrawable(context, R.drawable.bg_corners_green)
            WinnerColor.RED.color -> background =
                ContextCompat.getDrawable(context, R.drawable.bg_corners_red)
            WinnerColor.BLUE.color -> background =
                ContextCompat.getDrawable(context, R.drawable.bg_corners_blue)
        }
    }

    fun hideSoftKeyboard(activity: Activity, view: View) {
        val inputMethodManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}