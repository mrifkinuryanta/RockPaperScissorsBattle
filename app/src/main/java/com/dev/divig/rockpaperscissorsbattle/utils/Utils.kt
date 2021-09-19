package com.dev.divig.rockpaperscissorsbattle.utils

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.dev.divig.rockpaperscissorsbattle.R
import com.dev.divig.rockpaperscissorsbattle.enum.WinnerColor
import com.google.android.material.snackbar.Snackbar

object Utils {
    fun showSnackBar(context: Context, view: View, msg: String) {
        snackBar(context, view, msg, R.color.black_6F)
    }

    private fun snackBar(context: Context, view: View, msg: String, colorBackground: Int) {
        val snackBar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
        val snackView = snackBar.view
        val tv = snackView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        tv.setTextColor(ContextCompat.getColor(context, R.color.white))
        snackBar.view.setBackgroundColor(
            ContextCompat.getColor(
                context,
                colorBackground
            )
        )
        snackBar.show()
    }

    fun View.setBackgroundAction(context: Context) {
        background = ContextCompat.getDrawable(context, R.drawable.bg_corners_brown)
    }

    fun View.setBackgroundColor(context: Context, color: Int) {
        when (color) {
            WinnerColor.GREEN.color -> background =
                ContextCompat.getDrawable(context, R.drawable.bg_corners_green)
            WinnerColor.RED.color -> background =
                ContextCompat.getDrawable(context, R.drawable.bg_corners_red)
            WinnerColor.BLUE.color -> background =
                ContextCompat.getDrawable(context, R.drawable.bg_corners_blue)
        }
    }
}