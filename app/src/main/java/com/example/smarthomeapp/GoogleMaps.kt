package com.example.smarthomeapp

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.location.Geocoder
import android.location.LocationManager
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.smarthomeapp.databinding.ActivityGoogleMapsBinding
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.Marker

class GoogleMaps : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityGoogleMapsBinding
    private lateinit var latitudeTextView: TextView
    private lateinit var longitudeTextView: TextView
    private lateinit var locationTextView: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private val SHARED_PREFS_KEY = "MySharedPreferences"
    private var mapLoaded = false

    companion object {
        private const val LOCATION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGoogleMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE)

        val cancel: AppCompatImageView = findViewById(R.id.location_cancel_button)
        cancel.setOnClickListener {
            finish()
        }
        val save: AppCompatImageView = findViewById(R.id.location_save_button)
        save.setOnClickListener {
            setLocation()
        }

        latitudeTextView = findViewById(R.id.latitudeTextView)
        longitudeTextView = findViewById(R.id.longitudeTextView)
        locationTextView = findViewById(R.id.locationTextView)

        // Set up other functionality as needed

        // Obtain the SupportMapFragment and get notified when the map is ready to be used
        val mapFragment = SupportMapFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.mapFrameLayout, mapFragment)
            .commit()

        //val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFrameLayout) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val searchEditText: EditText = findViewById(R.id.searchEditText)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val location = searchEditText.text.toString()
                updateMapWithLocation(location)
            }

            override fun afterTextChanged(s: Editable?) {}
        })


    }

    private fun setLocation() {

        val updateLocation = intent.getStringExtra("updateLocation")
        if (updateLocation == "update") {
            returnResult()
        } else {
            val editor = sharedPreferences.edit()
            val location = locationTextView.text.toString()
            editor.putString("LocationPrefs", location)
            editor.apply()

            val intent = Intent(this, CreateRoutine::class.java)
            startActivity(intent)
        }
    }

    private fun returnResult() {
        val result = locationTextView.text.toString()

        // Create an intent with the result data
        val resultIntent = Intent()
        resultIntent.putExtra("updateLocation", result)

        // Set the result and finish the activity
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    private fun updateMapWithLocation(location: String) {
        if (location.isNotEmpty()) {
            val geocoder = Geocoder(this)
            val addresses = geocoder.getFromLocationName(location, 1)
            if (addresses!!.isNotEmpty()) {
                val address = addresses[0]
                val latitude = address.latitude
                val longitude = address.longitude
                val latLng = LatLng(latitude, longitude)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f))
                mMap.addMarker(MarkerOptions().position(latLng).title(location))

                latitudeTextView.text = "Latitude: $latitude"
                longitudeTextView.text = "Longitude: $longitude"

                val locationName = address.getAddressLine(0)
                locationTextView.text = locationName
            }
            else {
                Toast.makeText(applicationContext, "Loading", Toast.LENGTH_LONG).show()
            }
        } else {
            // Clear the map and text views when the location is empty
            mMap.clear()
            latitudeTextView.text = "Latitude: "
            longitudeTextView.text = "Longitude: "
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mapLoaded = true

        // Enable the My Location layer, if the permission is granted
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true

            // Get the last known location of the device
            val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 14f))
                    val marker = mMap.addMarker(
                        MarkerOptions().position(currentLatLng).title("Marker at Current Location")
                            .draggable(true)
                    )
                    // Update the latitude and longitude TextViews with the current location
                    latitudeTextView.text = "Latitude: ${currentLatLng.latitude}"
                    longitudeTextView.text = "Longitude: ${currentLatLng.longitude}"

                    // Reverse geocoding to get the location string
                    val geocoder = Geocoder(this)
                    val addresses =
                        geocoder.getFromLocation(currentLatLng.latitude, currentLatLng.longitude, 1)
                    if (addresses!!.isNotEmpty()) {
                        val address = addresses[0]
                        val locationString = address.getAddressLine(0)
                        locationTextView.text = locationString
                    }

                    // Set up marker drag listener
                    mMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
                        override fun onMarkerDragStart(marker: Marker) {}

                        override fun onMarkerDrag(marker: Marker) {}

                        override fun onMarkerDragEnd(marker: Marker) {
                            val position = marker.position
                            latitudeTextView.text = "Latitude: ${position.latitude}"
                            longitudeTextView.text = "Longitude: ${position.longitude}"

                            // Reverse geocoding to get the location string
                            val geocoder = Geocoder(this@GoogleMaps)
                            val addresses =
                                geocoder.getFromLocation(position.latitude, position.longitude, 1)
                            if (addresses!!.isNotEmpty()) {
                                val address = addresses[0]
                                val locationString = address.getAddressLine(0)
                                locationTextView.text = locationString
                            }
                        }
                    })
                }
            }
        } else {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        }
    }

    override fun onPause() {
        super.onPause()
        mapLoaded = false
    }

    override fun onResume() {
        super.onResume()
        mapLoaded = true
    }


}