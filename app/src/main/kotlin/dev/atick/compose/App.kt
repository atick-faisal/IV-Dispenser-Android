package dev.atick.compose

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.orhanobut.logger.LogAdapter
import com.orhanobut.logger.Logger
import dagger.hilt.android.HiltAndroidApp
import dev.atick.mqtt.service.MqttService.Companion.MQTT_NOTIFICATION_CHANNEL_ID
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {
    @Inject
    lateinit var logAdapter: LogAdapter

    override fun onCreate() {
        super.onCreate()
        Logger.addLogAdapter(logAdapter)
        Logger.i("Skynet Initiated!")
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                MQTT_NOTIFICATION_CHANNEL_ID,
                "I/V Dispenser Status Notification",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Always Monitor I/V Dispenser Statuses in the Background"
            }
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}