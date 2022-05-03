package com.aneesh.chatkara.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.aneesh.chatkara.utils.CheckCredentials
import com.aneesh.chatkara.R
import com.aneesh.chatkara.utils.AlertBoxNetwork
import com.aneesh.chatkara.utils.ConnectionManager
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    lateinit var login_mobile : EditText
    lateinit var login_password : EditText
    lateinit var login_btn : Button
    lateinit var login_forgotPassword : TextView
    lateinit var login_register : TextView
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        onBindView()
        btnLoginListener()
        txtSignUpListener()
        txtForgotPasswordListener()
    }

    private fun txtForgotPasswordListener() {
        login_forgotPassword.setOnClickListener{
            val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun txtSignUpListener() {
        login_register.setOnClickListener{
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun btnLoginListener() {
        login_btn.setOnClickListener {
            var mobile = login_mobile.text.toString()
            var password = login_password.text.toString()

            val checkCredentials = CheckCredentials(this@LoginActivity, mobile, "", password)

            if(checkCredentials.checkMobile() && checkCredentials.checkPassword()){
                validateCredentials(mobile, password)
            }
        }
    }

    private fun validateCredentials(mobile: String, password: String) {
        val queue = Volley.newRequestQueue(this@LoginActivity)
        val url = "http://13.235.250.119/v2/login/fetch_result/"

        val jsonParams = JSONObject()
        jsonParams.put("mobile_number", login_mobile.text)
        jsonParams.put("password", login_password.text)

        if(ConnectionManager().checkConnectivity(this@LoginActivity)){
            val jsonReqest = object : JsonObjectRequest(Request.Method.POST, url, jsonParams,
                Response.Listener {

                    try{
                        val success = it.getJSONObject("data").getBoolean("success")
                        if(success){
                            val jsonObject = it.getJSONObject("data").getJSONObject("data")
                            sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
                            sharedPreferences.edit().putString("user_id", jsonObject.getString("user_id")).apply()
                            sharedPreferences.edit().putString("name", jsonObject.getString("name")).apply()
                            sharedPreferences.edit().putString("email", jsonObject.getString("email")).apply()
                            sharedPreferences.edit().putString("mobile_number", jsonObject.getString("mobile_number")).apply()
                            sharedPreferences.edit().putString("address",jsonObject.getString("address")).apply()
                            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                            startActivity(intent)
                            finish()
                        }else{
                            val msg = it.getJSONObject("data").getString("errorMessage")
                            Toast.makeText(this@LoginActivity,msg, Toast.LENGTH_SHORT).show()
                        }
                    }catch(e : JSONException){
                        Toast.makeText(this@LoginActivity, "Some error occurred! Please try again", Toast.LENGTH_SHORT).show()
                    }

                }, Response.ErrorListener {
                    Toast.makeText(this@LoginActivity, "Some error occurred! Please try again", Toast.LENGTH_SHORT).show()
                }){

                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "5ab5cb50f5fbd6"
                    return headers
                }
            }
            queue.add(jsonReqest)
        }else{
            AlertBoxNetwork().createDialog(this@LoginActivity, this)
        }
    }

    private fun onBindView() {
        login_mobile = findViewById(R.id.login_mobile)
        login_password = findViewById(R.id.login_password)
        login_btn = findViewById(R.id.login_btn)
        login_forgotPassword = findViewById(R.id.login_forgotPassword)
        login_register = findViewById(R.id.login_register)
        sharedPreferences = getSharedPreferences(getString(R.string.shared_preference), Context.MODE_PRIVATE)
    }

    override fun onPause() {
        Log.d("destroy", "pause")
        super.onPause()
    }

    override fun onStop() {
        Log.d("destroy", "stop")
        super.onStop()
    }
    override fun onDestroy() {
        Log.d("destroy", "destroy")
        super.onDestroy()
    }
}