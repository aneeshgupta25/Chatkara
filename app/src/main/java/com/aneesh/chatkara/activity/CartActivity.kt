package com.aneesh.chatkara.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.aneesh.chatkara.R
import com.aneesh.chatkara.adapter.CartAdapter
import com.aneesh.chatkara.database.ItemEntites
import com.aneesh.chatkara.database.ResDatabase
import com.aneesh.chatkara.model.Item
import com.aneesh.chatkara.utils.AlertBoxNetwork
import com.aneesh.chatkara.utils.ConnectionManager
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class CartActivity : AppCompatActivity() {

    private lateinit var toolbar : Toolbar
    private lateinit var tvResName : TextView
    private lateinit var recycler : RecyclerView
    private lateinit var btnPlace : Button
    private lateinit var progressLayout : RelativeLayout
    private lateinit var progressBar : ProgressBar
    lateinit var name : String
    private var foodList = arrayListOf<Item>()
    lateinit var id : String
    private var cost : Int = 0
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        onBindView()
        setUpToolBar()
        setUpTheList()
        setBtnPlace()
        progressLayout.visibility = View.GONE
    }

    private fun setBtnPlace() {
        btnPlace.text = "Place Order (Total: Rs. ${cost})"
        btnPlace.setOnClickListener{

            val queue = Volley.newRequestQueue(this@CartActivity)
            val url = "http://13.235.250.119/v2/place_order/fetch_result/"

            val jsonParams = JSONObject()
            jsonParams.put("user_id", sharedPreferences.getString("user_id", null).toString())
            jsonParams.put("restaurant_id", id)
            jsonParams.put("total_cost", cost.toString())

            val foodArray = JSONArray()
            for(i in 0 until foodList.size){
                val jsonObject = JSONObject()
                jsonObject.put("food_item_id", foodList[i].id)
                foodArray.put(i, jsonObject)
            }
            jsonParams.put("food", foodArray)

            if(ConnectionManager().checkConnectivity(this@CartActivity)){

                try {
                    val jsonRequest = object : JsonObjectRequest(Request.Method.POST, url, jsonParams,
                        Response.Listener{
                            val success = it.getJSONObject("data").getBoolean("success")
                            if(success){
                                ClearList(this@CartActivity, id).execute()
                                val dialog = Dialog(this@CartActivity,
                                    android.R.style.Theme_Black_NoTitleBar_Fullscreen)
                                dialog.setContentView(R.layout.order_placed)
                                dialog.show()
                                dialog.setCancelable(false)
                                val btnOk = dialog.findViewById<Button>(R.id.btnOk)
                                btnOk.setOnClickListener{
                                    val intent = Intent(this@CartActivity, HomeActivity::class.java)
                                    startActivity(intent)
                                    dialog.dismiss()
                                }
                            }else{
                                Toast.makeText(this@CartActivity, "Something went wrong!", Toast.LENGTH_SHORT).show()
                            }
                        }, Response.ErrorListener {
                            Toast.makeText(this@CartActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
                        }){
                        override fun getHeaders(): MutableMap<String, String> {
                            val headers = HashMap<String, String>()
                            headers["Content-Type"] = "application/json"
                            headers["token"] = "5ab5cb50f5fbd6"
                            return headers
                        }
                    }
                    queue.add(jsonRequest)
                }
                catch(e : JSONException){
                    Toast.makeText(this@CartActivity, "Some unexpected error occurred", Toast.LENGTH_SHORT).show()
                }
            }else{
                AlertBoxNetwork().createDialog(this@CartActivity, this)
            }
        }
    }

    private fun setUpTheList() {
        Log.d("cart", "yahan aaya")
        val itemEntityList = RetrieveList(applicationContext).execute().get()
        for(element in itemEntityList){
            foodList.addAll(
                Gson().fromJson(element.foodItems, Array<Item>::class.java).asList()
            )
        }
        for(element in foodList){
            cost += element.cost_for_one.toInt()
        }
        Log.d("cart", "yahan bhi aaya")
        if(foodList.isEmpty()){
            Log.d("cart", "if mein aaya")
            Toast.makeText(this@CartActivity, "Empty List", Toast.LENGTH_SHORT).show()
        }else{
            Log.d("cart", "else mein aaya")
            val layoutManager = LinearLayoutManager(this@CartActivity)
            val recyclerAdapter = CartAdapter(this@CartActivity, foodList)
            recycler.adapter = recyclerAdapter
            recycler.layoutManager = layoutManager
        }
    }

    private fun setUpToolBar() {
        name = intent.getBundleExtra("data")?.getString("name").toString()
        id = intent.getBundleExtra("data")?.getString("id").toString()
        setSupportActionBar(toolbar)
        supportActionBar?.title = "My Cart"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        tvResName.text = name
    }

    private fun onBindView() {
        toolbar = findViewById(R.id.toolbar)
        tvResName = findViewById(R.id.tvResName)
        recycler = findViewById(R.id.recycler)
        btnPlace = findViewById(R.id.btnPlace)
        progressBar = findViewById(R.id.progressBar)
        progressLayout = findViewById(R.id.progressLayout)
        sharedPreferences = getSharedPreferences(getString(R.string.shared_preference), MODE_PRIVATE)
    }

    override fun onSupportNavigateUp(): Boolean {
        return if(ClearList(this@CartActivity, id).execute().get()){
            onBackPressed()
            true
        } else
            false
    }

    override fun onBackPressed() {
        ClearList(this@CartActivity, id).execute()
        super.onBackPressed()
    }

    override fun onStop() {
        ClearList(this@CartActivity, id).execute()
        super.onStop()
    }
    override fun onDestroy() {
        Log.d("Called", "Called")
        ClearList(this@CartActivity, id).execute()
        super.onDestroy()
    }

    class RetrieveList(val context : Context) : AsyncTask<Void, Void, List<ItemEntites>>(){
        val db = Room.databaseBuilder(context, ResDatabase::class.java, "res_db").build()
        override fun doInBackground(vararg params: Void?): List<ItemEntites>? {
            return db.itemDao().getAllItems()
        }
    }

    class ClearList(val context: Context, private val resId: String) : AsyncTask<Void, Void, Boolean>(){
        val db = Room.databaseBuilder(context, ResDatabase::class.java, "res_db").build()
        override fun doInBackground(vararg params: Void?): Boolean {
            db.itemDao().deleteOrders(resId)
            db.close()
            return true
        }
    }
}