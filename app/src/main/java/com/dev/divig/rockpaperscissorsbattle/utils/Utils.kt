package com.dev.divig.rockpaperscissorsbattle.utils

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.dev.divig.rockpaperscissorsbattle.R
import com.google.android.material.snackbar.Snackbar

object Utils {
    fun showToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun showToastLong(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

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

    fun ImageView.setBackground(context: Context) {
        background = ContextCompat.getDrawable(context, R.drawable.corners_button)
    }
}