package com.example.smarthomeapp.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.smarthomeapp.CreateRoutine
import com.example.smarthomeapp.R




class SelectThingsNotificationsFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_select_things_notifications, container, false)

        val tvNotificationsText = view.findViewById<TextView>(R.id.tvNotificationsText)

        tvNotificationsText.setOnClickListener {
            val intent = Intent(requireContext(), CreateRoutine::class.java)
            intent.putExtra("notificationCreated", true)
            startActivity(intent)
        }

        return view
    }




}