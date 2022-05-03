package com.aneesh.chatkara.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aneesh.chatkara.R
import com.aneesh.chatkara.model.RestaurantOrder

class OrderHistoryAdapter(val context : Context, private val restaurantList : MutableList<RestaurantOrder>) : RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder>() {

    init {
        Log.v("aneesh", "${itemCount}")
    }

    class OrderHistoryViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val orderResName : TextView = view.findViewById(R.id.orderResName)
        val orderDate : TextView = view.findViewById(R.id.orderDate)
        val foodRecycler : RecyclerView = view.findViewById(R.id.foodRecycler)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHistoryViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.order_history_view, parent, false)
        return OrderHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderHistoryViewHolder, position: Int) {
        val restaurant = restaurantList[position]
        holder.orderResName.text = restaurant.restaurant_name
        holder.orderDate.text = restaurant.order_placed_at

        val foodRecyclerAdapter = CartAdapter(context, restaurant.food_items)
        val foodLayoutManager = LinearLayoutManager(context)
        holder.foodRecycler.adapter = foodRecyclerAdapter
        holder.foodRecycler.layoutManager = foodLayoutManager

    }

    override fun getItemCount(): Int {
        return restaurantList.size
    }

}