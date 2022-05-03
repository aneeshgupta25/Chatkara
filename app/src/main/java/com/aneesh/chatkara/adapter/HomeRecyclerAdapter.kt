package com.aneesh.chatkara.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.aneesh.chatkara.R
import com.aneesh.chatkara.activity.ListActivity
import com.aneesh.chatkara.database.ResEntities
import com.aneesh.chatkara.fragment.HomeFragment
import com.aneesh.chatkara.model.Restaurant
import com.squareup.picasso.Picasso
import kotlin.collections.ArrayList

class HomeRecyclerAdapter(val context : Context, val activity : Activity, private val list : MutableList<Restaurant>, val resEntityList : ArrayList<ResEntities>) : RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.restaurant_view, parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.imgRecycler.clipToOutline = true
        }

        val restaurant = list[position]
        holder.txtName.text = restaurant.name
        holder.txtPrice.text = restaurant.cost_for_one
        holder.txtRating.text = restaurant.rating
        Picasso.get().load(restaurant.image).error(R.drawable.default_food).into(holder.imgRecycler)

        Log.d("list", "$position ${holder.txtName.text}")
        var resEntity = resEntityList[position]
        val async = HomeFragment.DBAsyncTask(context, resEntity, 1, restaurant).execute().get()
        if(async) {
            holder.favHeart.setImageResource(R.drawable.fav_heart)
        }else{
            holder.favHeart.setImageResource(R.drawable.ic_baseline_favorite_24)
        }

        holder.llContent.setOnClickListener{

            val id = restaurant.id
            val name = restaurant.name
            val intent = Intent(context, ListActivity::class.java)
            intent.putExtra("id", id)
            intent.putExtra("name", name)
            context.startActivity(intent)
        }

        holder.favHeart.setOnClickListener{

            //fetch position
            //val entity = list.get(position)

            Log.v("aneeshguptahehe","${HomeFragment.DBAsyncTask(context, resEntity, 1, restaurant).execute().get()}")

            if(HomeFragment.DBAsyncTask(context, resEntity, 1, restaurant).execute().get()){
                val removeFav = HomeFragment.DBAsyncTask(context, resEntity, 3, restaurant).execute().get()
                if(removeFav){
                    holder.favHeart.setImageResource(R.drawable.ic_baseline_favorite_24)
                }else{
                    Toast.makeText(context, "Some error occurred", Toast.LENGTH_SHORT).show()
                }
            }else{
                Log.d("ItemPassed", "${resEntity.id} resEntity name : ${resEntity.name} holder name : ${holder.txtName.text}")
                val addedFav = HomeFragment.DBAsyncTask(context, resEntity, 2, restaurant).execute().get()
                if(addedFav){
                    holder.favHeart.setImageResource(R.drawable.fav_heart)
                }else{
                    Toast.makeText(context, "Some error occurred", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val txtName : TextView = view.findViewById(R.id.txtName)
        val txtPrice : TextView = view.findViewById(R.id.txtPrice)
        val txtRating : TextView = view.findViewById(R.id.txtRating)
        val imgRecycler : ImageView = view.findViewById(R.id.imgRecycler)
        val favHeart : ImageView = view.findViewById(R.id.favHeart)
        val llContent : LinearLayout = view.findViewById(R.id.llContent)
    }

}