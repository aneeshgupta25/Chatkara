package com.aneesh.chatkara.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.aneesh.chatkara.R
import com.aneesh.chatkara.adapter.HomeRecyclerAdapter
import com.aneesh.chatkara.database.ResDatabase
import com.aneesh.chatkara.database.ResEntities
import com.aneesh.chatkara.model.Restaurant
import com.aneesh.chatkara.utils.AlertBoxNetwork
import com.aneesh.chatkara.utils.ConnectionManager
import com.aneesh.chatkara.utils.DrawerLocker
import com.aneesh.chatkara.utils.Sorter
import org.json.JSONException
import java.util.*
import kotlin.collections.HashMap

class HomeFragment : Fragment() {

    lateinit var homeRecycler : RecyclerView
    var list = arrayListOf<Restaurant>()
    var listEntity = arrayListOf<ResEntities>()
    lateinit var recyclerAdapter : HomeRecyclerAdapter
    lateinit var recyclerLayoutManager : LinearLayoutManager
    lateinit var progressLayout : RelativeLayout
    lateinit var progressBar : ProgressBar
    var checkedItem : Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        (activity as DrawerLocker).setDrawerEnabled(true)
        onBindView(view)
        setHasOptionsMenu(true)
        progressLayout.visibility = View.VISIBLE
        sendGETRequest(view)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.menu_dashboard, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.action_sort -> { showDialog(context) }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDialog(context: Context?) {
        val dialog = AlertDialog.Builder(context)
        dialog.setTitle("Sort by?")
        dialog.setSingleChoiceItems(R.array.filters, checkedItem){_,itemChecked ->
            checkedItem = itemChecked
        }
        dialog.setPositiveButton("Ok"){_,_ ->
            when(checkedItem){
                0 -> {
                    Collections.sort(list, Sorter.costSorting)
                }
                1 -> {
                    Collections.sort(list, Sorter.costSorting)
                    list.reverse()
                }
                2 -> {
                    Collections.sort(list, Sorter.ratingSorting)
                    list.reverse()
                }
            }
            recyclerAdapter.notifyDataSetChanged()
        }
        dialog.setNegativeButton("Cancel"){_,_ ->

        }
        dialog.create().show()
    }

    private fun onBindView(view : View) {
        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)
        homeRecycler = view.findViewById(R.id.homeRecycler)
    }

    private fun sendGETRequest(view: View) {
        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/"


        if(ConnectionManager().checkConnectivity(activity as Context)) {

            val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener {

                    progressLayout.visibility = View.GONE
                    try{
                        val success = it.getJSONObject("data").getBoolean("success")
                        if (success) {

                            lateinit var resEntity : ResEntities
                            var data = it.getJSONObject("data").getJSONArray("data")
                            for (i in 0 until data.length()) {
                                val resJsonObject = data.getJSONObject(i)
                                val resObject = Restaurant(
                                    resJsonObject.getString("id"),
                                    resJsonObject.getString("name"),
                                    resJsonObject.getString("rating"),
                                    resJsonObject.getString("cost_for_one"),
                                    resJsonObject.getString("image_url")
                                )
                                list.add(resObject)

                                resEntity = ResEntities(
                                    resObject.id,
                                    resObject.name,
                                    resObject.rating,
                                    resObject.cost_for_one,
                                    resObject.image
                                )

                                listEntity.add(resEntity)

                            }
                            if(activity != null){
                                recyclerAdapter =
                                    HomeRecyclerAdapter(activity as Context, activity as Activity, list, listEntity)
                                recyclerLayoutManager = LinearLayoutManager(activity)
                                homeRecycler.adapter = recyclerAdapter
                                homeRecycler.layoutManager = recyclerLayoutManager
                            }

                        } else {
                            Toast.makeText(context, "Some error occurred!", Toast.LENGTH_SHORT).show()
                        }
                    }catch(e : JSONException){
                        Toast.makeText(activity as Context, "Some unexpected occurred!!", Toast.LENGTH_SHORT).show()
                    }

                }, Response.ErrorListener {
                    Toast.makeText(activity as Context, "Volley Error Occurred!!", Toast.LENGTH_SHORT).show()
                }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "5ab5cb50f5fbd6"
                    return headers
                }
            }

            queue.add(jsonObjectRequest)
        }
        else{
            AlertBoxNetwork().createDialog(activity as Context, activity as Activity)
        }
    }

    class DBAsyncTask(context: Context, val resEntities: ResEntities, val mode: Int, val resObject: Restaurant?) : AsyncTask<Void, Void, Boolean>(){

        val db = Room.databaseBuilder(context, ResDatabase::class.java, "res_db").build()

        override fun doInBackground(vararg params: Void?): Boolean {

            when(mode){
                1 -> {
                    val res : ResEntities? = resObject?.let { db.resDao().getResByID(it.id) }
                    db.close()
                    return res != null
                }
                2 -> {
//                    Log.v("aneeshguptahehe","mode 2 mein aya")
//                    db.resDao().getAllRes().forEach {
//                        Log.v("aneeshguptahehe","${it.id}, ${it.name}")
//                    }
//                    Log.v("aneeshguptahehe","${resEntities.id}, ${resEntities.name}")
                    db.resDao().insert(resEntities)
                    db.close()
                    return true
                }
                3 -> {
                    db.resDao().delete(resEntities)
                    db.close()
                    return true
                }
            }

            return false

        }

    }

}