package com.example.smarthomeapp.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.viewpager.widget.ViewPager
import com.example.smarthomeapp.Adapter.MyAdapter
import com.example.smarthomeapp.R
import com.example.smarthomeapp.SelectAction
import com.example.smarthomeapp.SelectRoutine
import com.google.android.material.tabs.TabLayout


class SelectThingsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_select_things, container, false)

        val addNotificationLayout = view?.findViewById<LinearLayout>(R.id.add_notification)

        // Handle click events for the add notification layout
        addNotificationLayout?.setOnClickListener {
            val intent = Intent(activity, SelectAction::class.java)
            startActivity(intent)
        }

        return view;
    }

    private fun replaceFragment(fragment: Fragment){
        if(fragment != null){
            val transaction = childFragmentManager.beginTransaction()
            transaction.replace(R.id.viewPager, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

}