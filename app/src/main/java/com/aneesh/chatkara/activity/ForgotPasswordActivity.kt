package com.aneesh.chatkara.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.aneesh.chatkara.utils.CheckCredentials
import com.aneesh.chatkara.R
import com.aneesh.chatkara.utils.AlertBoxNetwork
import com.aneesh.chatkara.utils.ConnectionManager
import org.json.JSONObject

class ForgotPasswordActivity : AppCompatActivity() {

    lateinit var etMobile : EditText
    lateinit var etMail : EditText
    lateinit var btnNEXT : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        onBindView()
        btnNEXTListener()

    }

    private fun btnNEXTListener() {
        btnNEXT.setOnClickListener{

            val mobile = etMobile.text.toString()
            val mail = etMail.text.toString()
            val checkCredentials = CheckCredentials(this@ForgotPasswordActivity, mobile, mail, "")

            if(checkCredentials.checkMobile() && checkCredentials.checkEmail()){

                val queue = Volley.newRequestQueue(this@ForgotPasswordActivity)
                val url = "http://13.235.250.119/v2/forgot _password/fetch_result/"
                val jsonParams = JSONObject()
                jsonParams.put("mobile_number", mobile)
                jsonParams.put("email", mail)

                if(ConnectionManager().checkConnectivity(this@ForgotPasswordActivity)){
                    val jsonRequest = object : JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
                        val success = it.getJSONObject("data").getBoolean("success")
                        if(success){
                            val first_try = it.getJSONObject("data").getBoolean("first_try")
                            if(first_try){
                                Toast.makeText(this@ForgotPasswordActivity,
                                    "OTP has been sent to your registered mailID",
                                    Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@ForgotPasswordActivity, ResetPasswordActivity::class.java)
                                val bundle = Bundle()
                                bundle.putString("contact", mobile)
                                intent.putExtras(bundle)
                                startActivity(intent)
                            }else{
                                Toast.makeText(this@ForgotPasswordActivity, "Error! Please try again!", Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            Toast.makeText(this@ForgotPasswordActivity, "Entered Credentials are wrong", Toast.LENGTH_SHORT).show()
                        }
                    }, Response.ErrorListener {
                            Toast.makeText(this@ForgotPasswordActivity, "Some error occurred", Toast.LENGTH_SHORT).show()
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
                    AlertBoxNetwork().createDialog(this@ForgotPasswordActivity, this)
                }

            }
        }
    }

    private fun onBindView() {
        etMobile = findViewById(R.id.etMobile)
        etMail = findViewById(R.id.etMail)
        btnNEXT = findViewById(R.id.btnNEXT)
    }
}