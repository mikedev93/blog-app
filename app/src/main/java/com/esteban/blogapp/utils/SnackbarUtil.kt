package com.esteban.blogapp.utils

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.esteban.blogapp.R
import com.google.android.material.snackbar.Snackbar

object SnackbarUtil {

    fun displayError(context: Context, view: View, message: String) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG).apply { anchorView = view }
        snackbar.view.setBackgroundColor(ContextCompat.getColor(context, R.color.design_default_color_error))
        val textView = snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_warning_white, 0, 0, 0);
        textView.compoundDrawablePadding = 24
        snackbar.show()
    }

    fun displaySuccess(context: Context, view: View, message: String) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG).apply { anchorView = view }
        snackbar.view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorSuccessSnackbar))
        val textView = snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_check_circle_white, 0, 0, 0);
        textView.compoundDrawablePadding = 24
        snackbar.show()
    }
}