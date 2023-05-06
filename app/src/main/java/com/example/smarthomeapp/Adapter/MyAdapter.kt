package com.example.smarthomeapp.Adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.smarthomeapp.fragments.SelectRoutineFragment
import com.example.smarthomeapp.fragments.SelectSceneFragment
import com.example.smarthomeapp.fragments.SelectThingsFragment

internal class MyAdapter(var context: Context, fm: FragmentManager, var totalTabs: Int) :
    FragmentPagerAdapter(fm) {


    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                SelectThingsFragment()
            }
            1 -> {
                SelectSceneFragment()
            }
            2 -> {
                SelectRoutineFragment()
            }
            else -> getItem(position)
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }


}