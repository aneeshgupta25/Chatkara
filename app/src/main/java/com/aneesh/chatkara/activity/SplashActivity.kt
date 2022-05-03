package com.aneesh.chatkara.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.aneesh.chatkara.R

class SplashActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        sharedPreferences = getSharedPreferences(getString(R.string.shared_preference), Context.MODE_PRIVATE)

        Handler().postDelayed(Runnable {
            checkLoginStatus()
            finish()
        }, 1000)

    }

    private fun checkLoginStatus() {
        var checkLogin = sharedPreferences.getBoolean("isLoggedIn", false)
        if(checkLogin){
            val intent = Intent(this@SplashActivity, HomeActivity::class.java)
            startActivity(intent)
        }else{
            val intent = Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}