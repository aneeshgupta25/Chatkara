package com.aneesh.chatkara.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.aneesh.chatkara.R
import com.aneesh.chatkara.activity.ListActivity
import com.aneesh.chatkara.database.ResEntities
import com.squareup.picasso.Picasso

class FavouriteRecyclerAdapter(val context : Context, var resEntityList : MutableList<ResEntities>, val favListener: FavListener) : RecyclerView.Adapter<FavouriteRecyclerAdapter.FragmentViewHolder>() {

    interface FavListener{
        fun favHeartListener(position: Int, resEntity: ResEntities)
    }

    class FragmentViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val txtName : TextView = view.findViewById(R.id.txtName)
        val txtPrice : TextView = view.findViewById(R.id.txtPrice)
        val txtRating : TextView = view.findViewById(R.id.txtRating)
        val imgRecycler : ImageView = view.findViewById(R.id.imgRecycler)
        val favHeart : ImageView = view.findViewById(R.id.favHeart)
        val llContent : LinearLayout = view.findViewById(R.id.llContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FragmentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.restaurant_view, parent, false)
        return FragmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: FragmentViewHolder, position: Int) {
        val resEntity = resEntityList[position]
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.imgRecycler.clipToOutline = true
        }
        Picasso.get().load(resEntity.image_url).error(R.drawable.default_food).into(holder.imgRecycler)
        holder.txtName.text = resEntity.name
        holder.txtPrice.text = resEntity.cost_for_one
        holder.txtRating.text = resEntity.rating
        holder.favHeart.setImageResource(R.drawable.fav_heart)

        holder.llContent.setOnClickListener{
            val id = resEntity.id
            val name = resEntity.name
            val intent = Intent(context, ListActivity::class.java)
            intent.putExtra("id", id)
            intent.putExtra("name", name)
            context.startActivity(intent)
        }

        holder.favHeart.setOnClickListener{
            favListener.favHeartListener(position, resEntity)
        }
    }

    override fun getItemCount(): Int {
        return resEntityList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}