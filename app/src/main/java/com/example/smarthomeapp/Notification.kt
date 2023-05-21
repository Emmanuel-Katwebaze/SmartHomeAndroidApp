package com.example.smarthomeapp

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.smarthomeapp.Database.DatabaseHandler
import java.util.*

class Notification : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val notificationId = intent.getIntExtra("notificationId", 0)
        val desiredLocation = intent.getStringExtra("locationExtra").toString()
        val databaseHandler: DatabaseHandler = DatabaseHandler(context.applicationContext)

        Log.i("notify1", notificationId.toString())
        Log.i("notify2", desiredLocation)

        // Create a LocationManager instance
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (desiredLocation == "None" || desiredLocation == "null") {
            // The current location matches the desired location, show the notification
            val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(intent.getStringExtra("routineExtra"))
                .setContentText(intent.getStringExtra("notificationExtra"))
                .build()

            val manager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(notificationId, notification)

            updateLastRun(notificationId, databaseHandler)
        } else {
            // Define a LocationListener to listen for location updates
            val locationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    val currentLocationName = getLocationName(context, location)

                    if (currentLocationName != null && currentLocationName == desiredLocation) {
                        // The current location matches the desired location, show the notification
                        val notification =
                            NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                                .setSmallIcon(R.drawable.ic_launcher_foreground)
                                .setContentTitle(intent.getStringExtra("routineExtra"))
                                .setContentText(intent.getStringExtra("notificationExtra"))
                                .build()

                        val manager =
                            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        manager.notify(notificationId, notification)
                        updateLastRun(notificationId, databaseHandler)
                    }
                }

                override fun onProviderDisabled(provider: String) {}
                override fun onProviderEnabled(provider: String) {}
                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            }
            // Check if location permissions are granted
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {

                // Location permissions are granted, request location updates
                locationManager.requestSingleUpdate(
                    LocationManager.NETWORK_PROVIDER,
                    locationListener,
                    null
                )
            } else {
                // Location permissions are not granted, start PermissionRequestActivity to request permissions
                val permissionIntent = Intent(context, MainActivity::class.java)
                val pendingIntent = PendingIntent.getActivity(
                    context,
                    1,
                    permissionIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )

            }
        }


    }

    private fun updateLastRun(routineId: Int, databaseHandler: DatabaseHandler) {
        val calendar: Calendar = Calendar.getInstance()

        // Current Date
        val year: Int = calendar.get(Calendar.YEAR)
        val month: Int = calendar.get(Calendar.MONTH) + 1 // Month is zero-based, so add 1
        val day: Int = calendar.get(Calendar.DAY_OF_MONTH)

        // Current Time
        val hour: Int = calendar.get(Calendar.HOUR_OF_DAY)
        val minute: Int = calendar.get(Calendar.MINUTE)
        val second: Int = calendar.get(Calendar.SECOND)

        // Create a formatted string with the date and time information
        val currentDateTime: String =
            String.format("%04d-%02d-%02d %02d:%02d:%02d", year, month, day, hour, minute, second)


        databaseHandler.updateLastRunById(routineId, currentDateTime)

    }

    // Utility function to get the location name based on the coordinates
    private fun getLocationName(context: Context, location: Location?): String? {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses =
            geocoder.getFromLocation(location?.latitude ?: 0.0, location?.longitude ?: 0.0, 1)
        if (addresses!!.isNotEmpty()) {
            val address = addresses[0]
            return address.getAddressLine(0)
        }
        return null
    }


    companion object {
        const val NOTIFICATION_CHANNEL_ID = "smartHaven_channel"
    }

}