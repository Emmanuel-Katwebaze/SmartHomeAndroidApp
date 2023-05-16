package com.example.smarthomeapp

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.smarthomeapp.R

class Notification: BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent)
    {
        val notification = NotificationCompat.Builder(context, "channel1")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(intent.getStringExtra("routineExtra"))
            .setContentText(intent.getStringExtra("notificationExtra"))
            .build()

        val  manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(1, notification)
    }

    companion object {
        const val COUNTER_CHANNEL_ID = "smartHaven_channel"
    }

}