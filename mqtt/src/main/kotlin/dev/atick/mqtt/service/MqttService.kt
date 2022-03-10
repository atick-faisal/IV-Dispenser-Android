package dev.atick.mqtt.service

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.orhanobut.logger.Logger
import dagger.hilt.android.AndroidEntryPoint
import dev.atick.core.service.BaseService
import dev.atick.core.utils.extensions.debugMessage
import dev.atick.data.database.room.DispenserDao
import dev.atick.mqtt.R
import dev.atick.mqtt.repository.MqttRepository
import javax.inject.Inject

@AndroidEntryPoint
class MqttService : BaseService() {

    companion object {
        const val PERSISTENT_NOTIFICATION_CHANNEL_ID = "dev.atick.mqtt.persistent"
        const val ALERT_NOTIFICATION_CHANNEL_ID = "dev.atick.mqtt.alert"
        const val ALERT_NOTIFICATION_ID = 1011
    }

    @Inject
    lateinit var dispenserDao: DispenserDao

    @Inject
    lateinit var mqttRepository: MqttRepository

    private val persistentNotificationBuilder = NotificationCompat.Builder(
        this, PERSISTENT_NOTIFICATION_CHANNEL_ID
    )

    private var serviceStarted = false
    private var notificationIntent: Intent? = null

    inner class LocalBinder : Binder() {
        fun getService(): MqttService = this@MqttService
    }

    private val binder = LocalBinder()

    override fun onCreateService() {
        super.onCreateService()
        mqttRepository.initializeClient(
            onConnected = {
                val notification = persistentNotificationBuilder
                    .setSmallIcon(R.drawable.ic_connected)
                    .setContentTitle(
                        getString(
                            R.string.persistent_notification_title
                        )
                    )
                    .setContentText(
                        getString(
                            R.string.persistent_notification_description
                        )
                    )
                    .build()
                with(NotificationManagerCompat.from(this)) {
                    notify(PERSISTENT_NOTIFICATION_ID, notification)
                }
            },
            onDisconnected = {
                val notification = persistentNotificationBuilder
                    .setSmallIcon(R.drawable.ic_warning)
                    .setContentTitle(
                        getString(
                            R.string.persistent_notification_warning_title
                        )
                    )
                    .setContentText(
                        getString(
                            R.string.persistent_notification_warning_description
                        )
                    )
                    .build()
                with(NotificationManagerCompat.from(applicationContext)) {
                    notify(PERSISTENT_NOTIFICATION_ID, notification)
                }
            }
        )
    }

    override fun onStartService(intent: Intent?) {
        Logger.i("STARTING MQTT SERVICE")
        val username = intent?.getStringExtra("username")
        val password = intent?.getStringExtra("password")
        mqttRepository.connect(username, password) {
            mqttRepository.subscribe(
                topic = "dev.atick.mqtt/status/#",
                onSubscribe = {},
                onMessage = {},
                onAlert = { handleAlert(it) }
            )
        }
        serviceStarted = true
    }

    override fun setupNotification(): Notification {
        try {
            notificationIntent = Intent(
                this, Class.forName("dev.atick.compose.MainActivity")
            )
        } catch (e: ClassNotFoundException) {
            Logger.i("MAIN ACTIVITY NOT FOUND!")
            e.printStackTrace()
        }
        val notification = if (
            mqttRepository.isClientConnected.value?.peekContent() == true
        ) {
            persistentNotificationBuilder
                .setSmallIcon(R.drawable.ic_connected)
                .setContentTitle(getString(R.string.persistent_notification_title))
                .setContentText(getString(R.string.persistent_notification_description))
                .setPriority(NotificationCompat.PRIORITY_LOW)
        } else {
            persistentNotificationBuilder
                .setSmallIcon(R.drawable.ic_warning)
                .setContentTitle(getString(R.string.persistent_notification_warning_title))
                .setContentText(getString(R.string.persistent_notification_warning_description))
                .setPriority(NotificationCompat.PRIORITY_LOW)
        }

        notificationIntent?.let {
            notification.apply {
                val pendingIntent = PendingIntent.getActivity(
                    this@MqttService,
                    0,
                    notificationIntent,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )
                setContentIntent(pendingIntent)
            }
        }

        return notification.build()
    }

    override fun collectGarbage() {
        mqttRepository.disconnect {
            debugMessage("Disconnected")
        }
        serviceStarted = false
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    private fun handleAlert(message: String) {
        val notification = NotificationCompat
            .Builder(this, ALERT_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_alert)
            .setContentTitle(getString(R.string.iv_dispenser_alert))
            .setContentText(message)

        notificationIntent?.let {
            notification.apply {
                val pendingIntent = PendingIntent.getActivity(
                    this@MqttService,
                    101,
                    notificationIntent,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )
                setContentIntent(pendingIntent)
            }
        }
        with(NotificationManagerCompat.from(this)) {
            notify(ALERT_NOTIFICATION_ID, notification.build())
        }
    }
}