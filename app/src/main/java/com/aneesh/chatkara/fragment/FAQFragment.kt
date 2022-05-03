package com.aneesh.chatkara.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aneesh.chatkara.R
import com.aneesh.chatkara.utils.DrawerLocker

class FAQFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_f_a_q, container, false)
        (activity as DrawerLocker).setDrawerEnabled(true)
        return view
    }
}