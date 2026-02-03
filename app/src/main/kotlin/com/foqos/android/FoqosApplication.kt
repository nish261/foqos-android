package com.foqos.android

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class FoqosApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        instance = this
        createNotificationChannels()
    }
    
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val sessionChannel = NotificationChannel(
                CHANNEL_SESSION,
                "Active Sessions",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Shows when a blocking session is active"
            }
            
            val alertChannel = NotificationChannel(
                CHANNEL_ALERTS,
                "Session Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Alerts for session start/stop"
            }
            
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(sessionChannel)
            notificationManager.createNotificationChannel(alertChannel)
        }
    }
    
    companion object {
        lateinit var instance: FoqosApplication
            private set
        
        const val CHANNEL_SESSION = "session_channel"
        const val CHANNEL_ALERTS = "alerts_channel"
    }
}
