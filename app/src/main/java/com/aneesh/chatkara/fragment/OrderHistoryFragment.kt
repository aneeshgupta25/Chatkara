package com.aneesh.chatkara.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.aneesh.chatkara.R
import com.aneesh.chatkara.adapter.OrderHistoryAdapter
import com.aneesh.chatkara.model.Item
import com.aneesh.chatkara.model.RestaurantOrder
import com.aneesh.chatkara.utils.ConnectionManager
import com.aneesh.chatkara.utils.DrawerLocker
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class OrderHistoryFragment : Fragment() {

    lateinit var recycler : RecyclerView
    lateinit var sharedPreferences: SharedPreferences
    lateinit var progressLayout : RelativeLayout
    lateinit var progressBar : ProgressBar
    lateinit var no_order_layout : RelativeLayout
    lateinit var order_layout : RelativeLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_order_history, container, false)
        (activity as DrawerLocker).setDrawerEnabled(true)
        onBindView(view)
        no_order_layout.visibility = View.GONE
        sharedPreferences = (activity as Context).getSharedPreferences(getString(R.string.shared_preference), Context.MODE_PRIVATE)
        val id = sharedPreferences.getString("user_id", "")

        if (id != null) {
            connectToServer(view, id)
        }
        return view

    }

    private fun onBindView(view : View) {
        recycler = view.findViewById(R.id.recycler)
        progressLayout  = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)
        no_order_layout = view.findViewById(R.id.no_order_layout)
        order_layout = view.findViewById(R.id.order_layout)
    }

    private fun connectToServer(view : View, id : String) {
        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v2/orders/fetch_result/${id}"

        if(ConnectionManager().checkConnectivity(activity as Context)){
            val jsonRequest = object : JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener{
                    val data = it.getJSONObject("data")
                    val success = data.getBoolean("success")
                    if(success){
                        progressLayout.visibility = View.GONE
                        val jsonArray = data.getJSONArray("data")
                        if(jsonArray.length() != 0){
                            val restaurantList = mutableListOf<RestaurantOrder>()
                            for(i in 0 until jsonArray.length()){
                                val jsonObject = jsonArray.getJSONObject(i)

                                val foodItems = arrayListOf<Item>()
                                val foodItemJsonArray = jsonObject.getJSONArray("food_items")
                                Log.d("size" , "foodItemJsonArray length is -> ${foodItemJsonArray.length()}")
                                for(j in 0 until foodItemJsonArray.length()){
                                    val obj = foodItemJsonArray.getJSONObject(j)
                                    foodItems.add(Item(obj.getString("food_item_id"),
                                        obj.getString("name"),
                                        obj.getString("cost")))
                                }

                                restaurantList.add(RestaurantOrder(jsonObject.get("order_id").toString(),
                                    jsonObject.get("restaurant_name").toString(),
                                    jsonObject.get("total_cost").toString(),
                                    formatDate(jsonObject.get("order_placed_at").toString()),
                                    foodItems
                                ))
                            }
                            Log.d("size", "${restaurantList.size}")
                            val recyclerAdapter = OrderHistoryAdapter(activity as Context, restaurantList)
                            val layoutManager = LinearLayoutManager(activity as Context)
                            recycler.adapter = recyclerAdapter
                            recycler.layoutManager = layoutManager
                        }else{
                            order_layout.visibility = View.GONE
                            no_order_layout.visibility = View.VISIBLE
                        }
                    }else{
                        Toast.makeText(context, "Some error occurred", Toast.LENGTH_SHORT).show()
                    }
                }, Response.ErrorListener {

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
    }

    private fun formatDate(dateString: String): String? {
        val inputFormatter = SimpleDateFormat("dd-MM-yy HH:mm:ss", Locale.ENGLISH)
        val date : Date = inputFormatter.parse(dateString) as Date

        val outputFormatter = SimpleDateFormat("dd/MM/yy", Locale.ENGLISH)
        return outputFormatter.format(date)
    }
}