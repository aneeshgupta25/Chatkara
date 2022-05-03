package com.aneesh.chatkara.utils

import android.content.Context
import android.util.Log
import android.widget.Toast

class CheckCredentials(val context : Context,
                       private val mobile : String,
                       private val mail : String,
                       private val password : String) {

    fun checkMobile() : Boolean{
        return when {
            mobile.isEmpty() -> {
                Toast.makeText(context, "Number can't be empty", Toast.LENGTH_SHORT).show()
                false
            }
            mobile.length < 10 -> {
                Toast.makeText(context, "Invalid Number", Toast.LENGTH_SHORT).show()
                false
            }
            else -> {
                true
            }
        }
    }

    fun checkEmail(): Boolean {
        Log.d("Aneesh", "${mail}")
        val checkMail =  android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches()
        return if(checkMail){
            true
        }else{
            Toast.makeText(context, "Invalid Mail ID", Toast.LENGTH_SHORT).show()
            false
        }
    }

    fun checkPassword() : Boolean{
        return if(password.isEmpty() || password.length < 4){
            Toast.makeText(context, "Password must be of at least 4 characters", Toast.LENGTH_SHORT).show()
            false
        }else{
            true
        }
    }

}