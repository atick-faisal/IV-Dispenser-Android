package dev.atick.core.service

import android.app.Notification
import android.app.Service
import android.content.Intent

abstract class BaseService : Service() {

    companion object {
        const val PERSISTENT_NOTIFICATION_ID = 101
    }

    private lateinit var persistentNotification: Notification

    abstract fun initService()
    abstract fun doInBackground()
    abstract fun setupNotification(): Notification
    abstract fun clear()

    override fun onCreate() {
        super.onCreate()
        initService()
        persistentNotification = setupNotification()
        startForeground(PERSISTENT_NOTIFICATION_ID, persistentNotification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        doInBackground()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        clear()
    }
}