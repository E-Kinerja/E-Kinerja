package com.arya.e_kinerja.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.showSnackBar(
    message: String,
    length: Int,
    actionMessage: CharSequence?,
    anchorView: View?,
    action: (View) -> Unit
) {
    val snackBar = Snackbar.make(this, message, length)

    when {
        actionMessage != null -> {
            snackBar.setAction(actionMessage) { action(this) }
        }
        anchorView != null -> {
            snackBar.anchorView = anchorView
        }
    }

    return snackBar.show()
}