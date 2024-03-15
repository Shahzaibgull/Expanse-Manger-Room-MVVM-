package com.example.mvvmroomdatabase.Signup_Login

import android.app.Activity
import android.app.AlertDialog
import android.view.Gravity
import com.example.mvvmroomdatabase.R

class loadingDialog(private val activity: Activity) {
    private var dialog: AlertDialog? = null

    fun startLoadingDialog() {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.custom_dialog, null))
        builder.setCancelable(true)
        dialog?.window?.setGravity(Gravity.CENTER)
        dialog = builder.create()
        dialog?.show()
    }

    fun dismissDialog() {
        dialog?.dismiss()
    }
}