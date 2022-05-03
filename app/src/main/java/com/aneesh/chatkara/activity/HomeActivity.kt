package com.aneesh.chatkara.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.android.volley.toolbox.Volley
import com.aneesh.chatkara.R
import com.aneesh.chatkara.fragment.*
import com.aneesh.chatkara.utils.DrawerLocker
import com.google.android.material.navigation.NavigationView

class HomeActivity : AppCompatActivity(),DrawerLocker {

    override fun setDrawerEnabled(enabled: Boolean) {
        val lockMode = if(enabled){
            DrawerLayout.LOCK_MODE_UNLOCKED
        }else{
            DrawerLayout.LOCK_MODE_LOCKED_CLOSED
        }
        drawerLayout.setDrawerLockMode(lockMode)
        actionBarDrawerToggle.isDrawerIndicatorEnabled = enabled
    }

    lateinit var drawerLayout : DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar : Toolbar
    lateinit var frame : FrameLayout
    var previousMenuItem : MenuItem? = null
    lateinit var sharedPreferences: SharedPreferences
    lateinit var actionBarDrawerToggle : ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        onBindView()
        setUpToolBar()
        setUpDrawer()
        actionBarDrawerToggle = object : ActionBarDrawerToggle(this@HomeActivity, drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        ){
            override fun onDrawerStateChanged(newState: Int) {
                super.onDrawerStateChanged(newState)
                val pendingRunnable= Runnable {
                    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                }
                Handler().postDelayed(pendingRunnable, 100)
            }
        }
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigationViewListener()
        openDashBoard()


    }

    private fun setUpDrawer() {
        val headerView = navigationView.getHeaderView(0)
        val headerName = headerView.findViewById<TextView>(R.id.drawerName)
        val headerImage = headerView.findViewById<ImageView>(R.id.imgDrawer)
        headerName.text = sharedPreferences.getString("name", "")

        headerName.setOnClickListener{
            navigationView.setCheckedItem(R.id.profile)
            toolbar?.title = "My Profile"
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame, ProfileFragment())
                .commit()
            val mPendingRunnable = Runnable { drawerLayout.closeDrawer(GravityCompat.START) }
            Handler().postDelayed(mPendingRunnable, 50)
        }

        headerImage.setOnClickListener{
            navigationView.setCheckedItem(R.id.profile)
            toolbar?.title = "My Profile"
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame, ProfileFragment())
                .commit()
            val mPendingRunnable = Runnable { drawerLayout.closeDrawer(GravityCompat.START) }
            Handler().postDelayed(mPendingRunnable, 50)
        }
        headerView.findViewById<TextView>(R.id.drawerNumber).text = "+91-${sharedPreferences.getString("mobile_number", "")}"

    }

    private fun navigationViewListener() {
        navigationView.setNavigationItemSelectedListener {

            if(previousMenuItem != null){
                previousMenuItem?.isChecked = false
            }

            it.isCheckable = true
            it.isChecked = true
            previousMenuItem = it

            val mPendingRunnable = Runnable { drawerLayout.closeDrawer(GravityCompat.START) }
            Handler().postDelayed(mPendingRunnable, 100)

            when(it.itemId){
                R.id.home -> {
                    openDashBoard()
                }
                R.id.profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, ProfileFragment())
                        .commit()

                    supportActionBar?.title = "My Profile"
                }
                R.id.favourites ->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, FavoritesFragment())
                        .commit()

                    supportActionBar?.title = "Favourite Restaurants"
                }
                R.id.orderHistory -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, OrderHistoryFragment())
                        .commit()

                    supportActionBar?.title = "My Previous Orders"
                }
                R.id.faq -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, FAQFragment())
                        .commit()

                    supportActionBar?.title = "FAQs"
                }
                R.id.logOut -> {
                    drawerLayout.closeDrawers()
                    val dialog = AlertDialog.Builder(this@HomeActivity)
                    dialog.setTitle("Confirmation")
                    dialog.setMessage("Are you sure you want to exit?")
                    dialog.setPositiveButton("YES"){_,_ ->
                        sharedPreferences.edit().putBoolean("isLoggedIn", false).apply()
                        sharedPreferences.edit().clear().apply()
                        startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
                        Volley.newRequestQueue(this).cancelAll(this::class.java.simpleName)
                        ActivityCompat.finishAffinity(this)
                    }
                    dialog.setNegativeButton("NO"){text, listener ->
                        openDashBoard()
                    }
                    dialog.create().show()
                }
            }
            return@setNavigationItemSelectedListener true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId
        //home is the id of the hamburger icon on the action bar
        if(id == android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START)
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setUpToolBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "All Restaurants"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun onBindView() {
        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        toolbar = findViewById(R.id.toolbar)
        frame = findViewById(R.id.frame)
        sharedPreferences = getSharedPreferences(getString(R.string.shared_preference), MODE_PRIVATE)
    }

    override fun onBackPressed() {
        when(supportFragmentManager.findFragmentById(R.id.frame)){
            !is HomeFragment -> {
                openDashBoard()
            }
            else ->{
                super.onBackPressed()
            }
        }
    }

    override fun onDestroy() {
        android.os.Process.killProcess(android.os.Process.myPid())
        super.onDestroy()
    }

    fun openDashBoard() {
        val fragment = HomeFragment()
        //beginTransaction method returns an instance
        val fragmentManager = supportFragmentManager.beginTransaction()

        fragmentManager.replace(R.id.frame, fragment).commit()
        supportActionBar?.title = "Home"

        navigationView.setCheckedItem(R.id.home)

        drawerLayout.closeDrawers()
    }

}