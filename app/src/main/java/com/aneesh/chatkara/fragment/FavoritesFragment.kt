package com.aneesh.chatkara.fragment

import android.app.AlertDialog
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.aneesh.chatkara.R
import com.aneesh.chatkara.adapter.FavouriteRecyclerAdapter
import com.aneesh.chatkara.database.ResDatabase
import com.aneesh.chatkara.database.ResEntities
import com.aneesh.chatkara.utils.DrawerLocker

class FavoritesFragment : Fragment(), FavouriteRecyclerAdapter.FavListener {

    lateinit var recycler : RecyclerView
    lateinit var recyclerAdapter : FavouriteRecyclerAdapter
    lateinit var progressLayout : RelativeLayout
    lateinit var progressBar : ProgressBar
    lateinit var resEntityList : MutableList<ResEntities>
    lateinit var no_fav_layout : RelativeLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_favouries, container, false)
        (activity as DrawerLocker).setDrawerEnabled(true)

        no_fav_layout = view.findViewById(R.id.no_fav_layout)
        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)
        recycler = view.findViewById(R.id.recycler)

        no_fav_layout.visibility = View.GONE
        progressLayout.visibility = View.VISIBLE
        displayList(view)

        return view
    }

    private fun displayList(view : View) {

        resEntityList = RetrieveFavourites(activity as Context).execute().get()
        if(resEntityList.isEmpty()){

            progressLayout.visibility = View.GONE
            no_fav_layout.visibility = View.VISIBLE

        }else{
            progressLayout.visibility = View.GONE
            recycler = view.findViewById(R.id.recycler)
            recyclerAdapter = FavouriteRecyclerAdapter(activity as Context, resEntityList, this)
            val layoutManager = LinearLayoutManager(activity)
            recycler.adapter = recyclerAdapter
            recycler.itemAnimator = DefaultItemAnimator()
            recycler.layoutManager = layoutManager
            recycler.setHasFixedSize(true)
        }

    }

    class RetrieveFavourites(val context : Context) : AsyncTask<Void, Void, MutableList<ResEntities>>(){
        override fun doInBackground(vararg params: Void?): MutableList<ResEntities> {
            val db = Room.databaseBuilder(context, ResDatabase::class.java, "res_db").build()
            return db.resDao().getAllRes()
        }

    }

    override fun favHeartListener(position: Int, resEntity: ResEntities) {
        val dialog = AlertDialog.Builder(context)
        dialog.setTitle("Attention")
        dialog.setMessage("Remove from favourites?")
        dialog.setPositiveButton("Remove"){text, listener ->
            val removeFav = HomeFragment.DBAsyncTask(activity as Context, resEntity, 3, null).execute().get()
            if(removeFav){
                Toast.makeText(context, "Removed from Favourites", Toast.LENGTH_SHORT).show()
                resEntityList.removeAt(position)
                recyclerAdapter.notifyItemRemoved(position)
                if(resEntityList.isEmpty()){
                    no_fav_layout.visibility = View.VISIBLE
                }
            }
            else{
                Toast.makeText(context, "Some error occurred", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.setNegativeButton("Cancel"){text, listener ->
            //do nothing
        }
        dialog.create().show()
    }
}