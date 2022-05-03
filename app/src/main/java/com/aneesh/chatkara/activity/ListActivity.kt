package com.aneesh.chatkara.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.aneesh.chatkara.R
import com.aneesh.chatkara.adapter.ItemsListAdapter
import com.aneesh.chatkara.database.ItemEntites
import com.aneesh.chatkara.database.ResDatabase
import com.aneesh.chatkara.model.Item
import com.aneesh.chatkara.utils.AlertBoxNetwork
import com.aneesh.chatkara.utils.ConnectionManager
import com.google.android.material.appbar.AppBarLayout
import com.google.gson.Gson
import org.json.JSONException

class ListActivity : AppCompatActivity() {

    lateinit var toolbar : Toolbar
    lateinit var appBar : AppBarLayout
    lateinit var list_recycler : RecyclerView
    lateinit var progressLayout : RelativeLayout
    lateinit var progressBar : ProgressBar
    lateinit var btnProceed: Button
    var orderList = arrayListOf<Item>()
    lateinit var id: String
    var name: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items_list)

        id = intent.getStringExtra("id").toString()
        name = intent.getStringExtra("name").toString()
        onBindView()
        setUpToolBar(name)
        progressLayout.visibility = View.VISIBLE
        btnProceed.visibility = View.GONE
        getItemList(id)

    }

    private fun getItemList(id : String?) {
        val queue = Volley.newRequestQueue(this@ListActivity)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/${id}/"

        if(ConnectionManager().checkConnectivity(this@ListActivity)){
            val jsonRequest = object : JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener {

                    try{
                        val success = it.getJSONObject("data").getBoolean("success")
                        if(success){
                            val list = arrayListOf<Item>()
                            val jsonArray = it.getJSONObject("data").getJSONArray("data")
                            for(i in 0 until jsonArray.length()){
                                val obj = jsonArray.getJSONObject(i)
                                val item = Item(obj.getString("id"),
                                    obj.getString("name"),
                                    obj.getString("cost_for_one"))
                                list.add(item)

                                progressLayout.visibility = View.GONE

                                var recyclerAdapter = ItemsListAdapter(this@ListActivity, list,
                                                        object : ItemsListAdapter.setUpItemListener{
                                                            override fun addItem(item: Item) {
                                                                orderList.add(item)
                                                                if(orderList.size > 0){
                                                                    btnProceed.visibility = View.VISIBLE
                                                                    btnProceedListener()
                                                                }
                                                            }

                                                            override fun removeItem(item: Item) {
                                                                orderList.remove(item)
                                                                if(orderList.isEmpty()){
                                                                    btnProceed.visibility = View.GONE
                                                                }
                                                            }

                                                        })
                                var layoutManager = LinearLayoutManager(this@ListActivity)
                                list_recycler.layoutManager = layoutManager
                                list_recycler.adapter = recyclerAdapter

                            }
                        }else{
                            Toast.makeText(this@ListActivity, "Something went wrong.", Toast.LENGTH_SHORT).show()
                        }
                    }catch (e : JSONException){
                        Toast.makeText(this@ListActivity, "$e", Toast.LENGTH_SHORT).show()
                    }

                }, Response.ErrorListener {
                    Toast.makeText(this@ListActivity, "Something went wrong!", Toast.LENGTH_SHORT).show()
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
            AlertBoxNetwork().createDialog(this@ListActivity, this)
        }
    }

    fun btnProceedListener() {
        Log.d("Proceed", "yahan aya")
        btnProceed.setOnClickListener{
            Log.d("Proceed button", "masti mein")
            val gson = Gson()
            val foodList = gson.toJson(orderList)
            Log.d("OrderList", "$id $foodList")

            val async = CartAsyncTask(this@ListActivity, id, foodList, 1).execute().get()
            if(async){
                val data = Bundle()
                data.putString("id", id)
                data.putString("name", name)
                val intent = Intent(this@ListActivity, CartActivity::class.java)
                intent.putExtra("data", data)
                startActivity(intent)
            }
            else{
                Toast.makeText(this@ListActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setUpToolBar(name : String?) {
        setSupportActionBar(toolbar)
        supportActionBar?.title = name
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun onBindView() {
        toolbar = findViewById(R.id.toolbar)
        list_recycler = findViewById(R.id.list_recycler)
        progressBar = findViewById(R.id.progressBar)
        progressLayout = findViewById(R.id.progressLayout)
        appBar = findViewById(R.id.appBar)
        btnProceed = findViewById(R.id.btnProceed)
    }

    class CartAsyncTask(val context: Context, val resId : String, val food_items : String, val mode: Int) : AsyncTask<Void, Void, Boolean>(){
        override fun doInBackground(vararg params: Void?): Boolean {
            val db = Room.databaseBuilder(context, ResDatabase::class.java, "res_db").build()
            when(mode){
                1 -> {
                    db.itemDao().insert(ItemEntites(resId, food_items))
                    db.close()
                    return true
                }
                2 -> {
                    db.itemDao().delete(ItemEntites(resId, food_items))
                    db.close()
                    return true
                }
            }
            return false
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onDestroy() {
        orderList.clear()
        super.onDestroy()
    }
    override fun onBackPressed() {
        if(orderList.isNotEmpty()){
            val dialog = AlertDialog.Builder(this@ListActivity)
            dialog.setTitle("Confirmation")
            dialog.setMessage("Going back will reset cart items. Do you still want to proceed?")
            dialog.setPositiveButton("YES"){_,_ ->
                orderList.clear()
                super.onBackPressed()
            }
            dialog.setNegativeButton("NO"){ _,_ ->
                //do nothing
            }
            dialog.create().show()
        }else{
            super.onBackPressed()
        }
    }

}