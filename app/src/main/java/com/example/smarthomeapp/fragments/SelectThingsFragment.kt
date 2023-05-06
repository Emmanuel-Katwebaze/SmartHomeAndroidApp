package com.example.smarthomeapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.smarthomeapp.CreateRoutine
import com.example.smarthomeapp.R


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
        val tvNotificationsText = view?.findViewById<TextView>(R.id.tvNotificationsText)

        val abSelectThing = activity?.findViewById<View>(R.id.abSelectThing)
        val abSelectAction = activity?.findViewById<View>(R.id.abSelectAction)

        // Handle click events for the add notification layout
        addNotificationLayout?.setOnClickListener {
            addNotificationLayout?.visibility = View.GONE
            abSelectThing?.visibility = View.GONE

            tvNotificationsText?.visibility = View.VISIBLE
            abSelectAction?.visibility = View.VISIBLE

        }


        tvNotificationsText?.setOnClickListener {
            val intent = Intent(requireContext(), CreateRoutine::class.java)
            intent.putExtra("notificationCreated", true)
            startActivity(intent)
        }


        return view
    }

    private fun replaceFragment(fragment: Fragment) {
        if (fragment != null) {
            val transaction = childFragmentManager.beginTransaction()
            transaction.replace(R.id.viewPager, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

}