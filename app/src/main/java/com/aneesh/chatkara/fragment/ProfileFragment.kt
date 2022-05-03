package com.aneesh.chatkara.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.aneesh.chatkara.R
import com.aneesh.chatkara.utils.DrawerLocker

class ProfileFragment : Fragment() {

    lateinit var tvName : TextView
    lateinit var tvContact : TextView
    lateinit var tvEmail : TextView
    lateinit var tvDelivery : TextView
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        (activity as DrawerLocker).setDrawerEnabled(true)
        onBindView(view)
        displayCredentials()

        return view
    }

    private fun displayCredentials() {
        tvName.text = sharedPreferences.getString("name", "")
        tvContact.text = "+91-${sharedPreferences.getString("mobile_number", "")}"
        tvEmail.text = sharedPreferences.getString("email", "")
        tvDelivery.text = sharedPreferences.getString("address", "")
    }

    private fun onBindView(view : View) {
        tvName = view.findViewById(R.id.tvName)
        tvContact = view.findViewById(R.id.tvContact)
        tvEmail = view.findViewById(R.id.tvEmail)
        tvDelivery = view.findViewById(R.id.tvDelivery)
        sharedPreferences =
            context?.getSharedPreferences(getString(R.string.shared_preference), Context.MODE_PRIVATE)!!
    }
}