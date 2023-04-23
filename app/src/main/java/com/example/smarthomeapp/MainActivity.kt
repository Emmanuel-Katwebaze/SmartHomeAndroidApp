package com.example.smarthomeapp


import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.smarthomeapp.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private val favoritesFragment = FavoritesFragment()
    private val thingsFragment = ThingsFragment()
    private val ideasFragment = IdeasFragment()
    private val routinesFragment = RoutinesFragment()
    private val settingsFragment = SettingsFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceFragment(favoritesFragment)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView.selectedItemId = R.id.favorites_navBtn;

        val selectedFragment = intent.getStringExtra("SELECTED_FRAGMENT")
        if (selectedFragment == "routines") {
            replaceFragment(routinesFragment)
            bottomNavigationView.selectedItemId = R.id.routines_navBtn;
        } else {
            replaceFragment(favoritesFragment)
        }


        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.favorites_navBtn -> {
                    replaceFragment(favoritesFragment)
                    true
                }
                R.id.things_navBtn -> {
                    replaceFragment(thingsFragment)
                    true;
                }
                R.id.routines_navBtn -> {
                    replaceFragment(routinesFragment)
                    true;
                }
                R.id.ideas_navBtn -> {
                    replaceFragment(ideasFragment)
                    true;
                }
                R.id.settings_navBtn -> {
                    replaceFragment(settingsFragment)
                    true;
                }
                else -> false
            }
        }


    }


    private fun replaceFragment(fragment: Fragment) {
        if (fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }
    }
}