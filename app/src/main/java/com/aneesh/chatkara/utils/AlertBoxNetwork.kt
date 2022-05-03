package com.aneesh.chatkara.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.core.app.ActivityCompat

class AlertBoxNetwork() {

    fun createDialog(context: Context, activity: Activity){
        val dialog = AlertDialog.Builder(context)
        dialog.setTitle("Error")
        dialog.setMessage("Internet Connection not found")
        dialog.setPositiveButton("Open Settings"){text, listener ->
            val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
            context.startActivity(intent)
            activity?.finish()
        }
        dialog.setNegativeButton("Exit"){text, listener ->
            ActivityCompat.finishAffinity(activity)
        }

    }

}