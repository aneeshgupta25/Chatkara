package com.aneesh.chatkara.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.aneesh.chatkara.R
import com.aneesh.chatkara.utils.AlertBoxNetwork
import com.aneesh.chatkara.utils.ConnectionManager
import org.json.JSONObject

class ResetPasswordActivity : AppCompatActivity() {

    lateinit var etOTP : EditText
    lateinit var etNewPassword : EditText
    lateinit var etConfirmPassword : EditText
    lateinit var btnSubmit : Button
    lateinit var progressLayout : RelativeLayout
    lateinit var progressBar : ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        onBindView()
        btnSubmitListener()
        progressLayout.visibility = View.GONE
    }

    private fun btnSubmitListener() {
        btnSubmit.setOnClickListener{
            if(etOTP.text.length == 4){
                if(etNewPassword.text.length >= 4){
                    if(etNewPassword.text == etNewPassword.text){

                        val contact = intent.getBundleExtra("contact").toString()
                        progressLayout.visibility = View.VISIBLE
                        val queue = Volley.newRequestQueue(this@ResetPasswordActivity)
                        val url = "http://13.235.250.119/v2/reset_password/fetch_result/"
                        val jsonParams = JSONObject()
                        jsonParams.put("mobile_number", contact)
                        jsonParams.put("password", etNewPassword.text)
                        jsonParams.put("otp", etOTP.text)

                        if(ConnectionManager().checkConnectivity(this@ResetPasswordActivity)){
                            val jsonRequest = object : JsonObjectRequest(Request.Method.POST, url, jsonParams,
                                Response.Listener {
                                    val success = it.getJSONObject("data").getBoolean("success")
                                    if(success){
                                        Toast.makeText(this@ResetPasswordActivity, "Password has successfully changed", Toast.LENGTH_SHORT).show()
                                        //Clearing all the shared preferences
                                        getSharedPreferences(getString(R.string.shared_preference), MODE_PRIVATE)
                                            .edit().clear().apply()
                                        val intent = Intent(this@ResetPasswordActivity, LoginActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }else{
                                        progressLayout.visibility = View.GONE
                                        Toast.makeText(this@ResetPasswordActivity, "Entered OTP does not match", Toast.LENGTH_SHORT).show()
                                    }
                            }, Response.ErrorListener {
                                    progressLayout.visibility = View.GONE
                                    Toast.makeText(this@ResetPasswordActivity, "Error! Please try again", Toast.LENGTH_SHORT).show()
                            }){

                                override fun getHeaders(): MutableMap<String, String> {
                                    val headers = HashMap<String, String>()
                                    headers["Content-Type"] = "application/json"
                                    headers["token"] = "5ab5cb50f5fbd6"
                                    return headers
                                }

                            }
                            queue.add(jsonRequest)
                        }else{
                            AlertBoxNetwork().createDialog(this@ResetPasswordActivity, this)
                        }
                    }else{
                        Toast.makeText(this@ResetPasswordActivity, "Password doesn't match", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this@ResetPasswordActivity, "Password must be of at least 4 characters", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this@ResetPasswordActivity, "OTP must be of 4 characters", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onBindView() {
        etOTP = findViewById(R.id.etOTP)
        etNewPassword = findViewById(R.id.etNewPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnSubmit = findViewById(R.id.btnSubmit)
        progressBar = findViewById(R.id.progressBar)
        progressLayout = findViewById(R.id.progressLayout)
    }
}