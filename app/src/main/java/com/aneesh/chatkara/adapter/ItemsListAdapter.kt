package com.aneesh.chatkara.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aneesh.chatkara.R
import com.aneesh.chatkara.model.Item

class ItemsListAdapter(val context : Context, val list : ArrayList<Item>, val listener : setUpItemListener) : RecyclerView.Adapter<ItemsListAdapter.ItemsListViewHolder>() {

    class ItemsListViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var tvSerial : TextView = view.findViewById(R.id.tvSerial)
        var tvItemName : TextView = view.findViewById(R.id.tvItemName)
        var tvItemPrice : TextView = view.findViewById(R.id.tvItemPrice)
        var btnAdd : Button = view.findViewById(R.id.btnAdd)
        var btnRemove : Button = view.findViewById(R.id.btnRemove)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_list_recycler, parent, false)
        return ItemsListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemsListViewHolder, position: Int) {
        val item = list[position]
        holder.tvSerial.text = (position + 1).toString()
        holder.tvItemName.text = item.name
        holder.tvItemPrice.text = item.cost_for_one
        holder.btnAdd.setOnClickListener{
            holder.btnAdd.visibility = View.GONE
            holder.btnRemove.visibility = View.VISIBLE
            listener.addItem(item)
        }
        holder.btnRemove.setOnClickListener{
            holder.btnRemove.visibility = View.GONE
            holder.btnAdd.visibility = View.VISIBLE
            listener.removeItem(item)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    interface setUpItemListener{
        fun addItem(item : Item)
        fun removeItem(item : Item)
    }

}