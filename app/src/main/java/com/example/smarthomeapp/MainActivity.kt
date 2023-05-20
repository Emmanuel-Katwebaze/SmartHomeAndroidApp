package com.example.smarthomeapp


import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.smarthomeapp.fragments.*
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView

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

        createNotificationChannel()
        getPermissions()

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


    private fun createNotificationChannel()
    {
        val name = "SmartHaven Channel"
        val desc = "Reminder notifications for day to day activities"
        val importance = NotificationManager.IMPORTANCE_HIGH

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(Notification.NOTIFICATION_CHANNEL_ID, name, importance)
            channel.description = desc

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

    }

    private fun getPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return
        } else {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        }
    }


}