package com.example.testseamfix.utils

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.appcompat.app.AlertDialog
import com.example.testseamfix.R

object Exts {

    private var progressDialog: ProgressDialog? = null

    @JvmName("showProgress1")
    fun showProgress(context: Activity, title: String?, message: String) {
        runOnMain {
            progressDialog?.dismiss()
            progressDialog = ProgressDialog.show(context, title, message)
        }
    }


    private fun dismissProgressf() {
        runOnMain {
            progressDialog?.dismiss()
        }
    }

    fun Activity.dismissProgress() = dismissProgressf()
    fun Activity.showProgress(title: String?, message: String) = showProgress(this, title, message)

    fun Activity.onErrorMessage(
        title: String,
        throwable: Throwable,
        listener: DialogInterface.OnClickListener? = null
    ) {
        dismissProgress()
        runOnMain {
            AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(if (throwable is StringIndexOutOfBoundsException) getString(R.string.invalid_response) else throwable.localizedMessage)
                .setNegativeButton(android.R.string.cancel, listener)
                .show()
        }
    }

    fun Activity.onSuccessMessage(message: Int, title: Int = R.string.successful) {
        dismissProgress()
        runOnMain {
            AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .show()
        }
    }
}