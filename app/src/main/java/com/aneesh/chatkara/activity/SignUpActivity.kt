package com.aneesh.chatkara.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.aneesh.chatkara.utils.CheckCredentials
import com.aneesh.chatkara.R
import com.aneesh.chatkara.utils.ConnectionManager
import org.json.JSONObject


class SignUpActivity : AppCompatActivity() {

    //Appcompat.widget.toolbar
    lateinit var toolbar : Toolbar
    lateinit var etName : EditText
    lateinit var etMail : EditText
    lateinit var etMobile : EditText
    lateinit var etDelivery : EditText
    lateinit var etChosePassword : EditText
    lateinit var etConfirmPassword : EditText
    lateinit var btnRegister : Button
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        onBindView()
        setupToolBar()
        btnRegisterlistener()
    }

    private fun btnRegisterlistener() {
        btnRegister.setOnClickListener{
            val name = etName.text.toString()
            val mail = etMail.text.toString()
            val phone = etMobile.text.toString()
            val deliveryAdd = etDelivery.text.toString()
            val chosePassword = etChosePassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()
            sharedPreferences = getSharedPreferences(getString(R.string.shared_preference), Context.MODE_PRIVATE)

            val checkCredentials = CheckCredentials(this@SignUpActivity, phone, mail, chosePassword)

            if(name.isEmpty() || name.length < 3){
                Toast.makeText(this@SignUpActivity, "Invalid Name", Toast.LENGTH_SHORT).show()
            }else if(!checkCredentials.checkEmail()){
                //Handled by the checkCredentials class
            }else if(!checkCredentials.checkMobile()){
                //Handled by the checkCredentials class
            }else if(deliveryAdd.isEmpty()){
                Toast.makeText(this@SignUpActivity, "Invalid Address!", Toast.LENGTH_SHORT).show()
            }else if(!checkCredentials.checkPassword()){
                //Handled by the checkCredentials class
            }else if(chosePassword != confirmPassword){
                Toast.makeText(this@SignUpActivity, "Password doesn't match", Toast.LENGTH_SHORT).show()
            }else{
                registerOnServer()
            }
        }
    }

    private fun registerOnServer() {
        val queue = Volley.newRequestQueue(this@SignUpActivity)
        val url = "http://13.235.250.119/v2/register/fetch_result/"

        val jsonParams = JSONObject()
        jsonParams.put("name", etName.text.toString())
        jsonParams.put("mobile_number", etMobile.text.toString())
        jsonParams.put("password", etChosePassword.text.toString())
        jsonParams.put("address", etDelivery.text.toString())
            jsonParams.put("email", etMail.text.toString())

        if(ConnectionManager().checkConnectivity(this@SignUpActivity)) {

            val jsonRequest =
                object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener {

                    val success = it.getJSONObject("data").getBoolean("success")
                    if(success){
                        val jsonObject = it.getJSONObject("data").getJSONObject("data")
                        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
                        sharedPreferences.edit().putString("user_id", jsonObject.getString("user_id")).apply()
                        sharedPreferences.edit().putString("name", jsonObject.getString("name")).apply()
                        sharedPreferences.edit().putString("email", jsonObject.getString("email")).apply()
                        sharedPreferences.edit().putString("mobile_number", jsonObject.getString("mobile_number")).apply()
                        sharedPreferences.edit().putString("address",jsonObject.getString("address")).apply()
                        val intent = Intent(this@SignUpActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        val errorMessage = it.getJSONObject("data").getString("errorMessage")
                        Toast.makeText(this@SignUpActivity, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }, Response.ErrorListener {
                    Toast.makeText(this@SignUpActivity, "Some unexpected error occurred. Please try again", Toast.LENGTH_SHORT).show()
                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-Type"] = "application/json"
                        headers["token"] = "5ab5cb50f5fbd6"
                        return headers
                    }
                }
            queue.add(jsonRequest)
        }else{
            Toast.makeText(this@SignUpActivity, "Some error occurred. Please try again", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupToolBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Register Yourself"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun onBindView() {
        toolbar = findViewById(R.id.toolbar)
        etName = findViewById(R.id.etName)
        etMail = findViewById(R.id.etMail)
        etMobile = findViewById(R.id.etMobile)
        etDelivery = findViewById(R.id.etDelivery)
        etChosePassword = findViewById(R.id.etChosePassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)
    }
}