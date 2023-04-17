package com.example.smarthomeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.example.smarthomeapp.Adapter.MyAdapter
import com.example.smarthomeapp.Adapter.SelectActionAdapter
import com.google.android.material.tabs.TabLayout

class SelectAction : AppCompatActivity() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_action)

        val toolbar = findViewById<Toolbar>(R.id.selectActionToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        tabLayout = findViewById(R.id.actionTabLayoutSelectThings)
        viewPager = findViewById(R.id.actionViewPager)

        tabLayout.addTab(tabLayout.newTab().setText("THINGS"))
        tabLayout.addTab(tabLayout.newTab().setText("SCENES"))
        tabLayout.addTab(tabLayout.newTab().setText("ROUTINES"))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        val adapter = SelectActionAdapter(this, supportFragmentManager, tabLayout.tabCount)
        viewPager.adapter = adapter

        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

    }
}