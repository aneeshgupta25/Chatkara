package com.aneesh.chatkara.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aneesh.chatkara.R
import com.aneesh.chatkara.model.Item

class CartAdapter(val context : Context, val list : ArrayList<Item>) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val tvName : TextView = view.findViewById(R.id.tvName)
        val tvItemPrice : TextView = view.findViewById(R.id.tvItemPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.cart_item_view, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val listItem = list[position]
        holder.tvName.text = listItem.name
        holder.tvItemPrice.text = listItem.cost_for_one
    }

    override fun getItemCount(): Int {
        return list.size
    }

}