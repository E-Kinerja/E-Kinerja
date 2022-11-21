package com.arya.e_kinerja.views

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.arya.e_kinerja.databinding.DialogLoadingBinding

fun createLoadingDialog(context: Context, layoutInflater: LayoutInflater): Dialog {
    val binding = DialogLoadingBinding.inflate(layoutInflater)
    val loadingDialog = Dialog(context)

    loadingDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    loadingDialog.setContentView(binding.root)
    loadingDialog.setCancelable(false)

    return loadingDialog
}