package com.apppillar.feature_home.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.edit
import com.apppillar.feature_home.R
import com.apppillar.feature_home.data.local.dao.DailyStepsDao
import com.apppillar.feature_home.data.local.entity.DailyStepsEntity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class StepCounterService : Service(), SensorEventListener {

    @Inject
    lateinit var dailyStepsDao: DailyStepsDao

    private lateinit var sensorManager: SensorManager
    private var stepSensor: Sensor? = null

    private lateinit var prefs: android.content.SharedPreferences
    private val date: String get() = LocalDate.now().toString()

    override fun onCreate() {
        super.onCreate()
        prefs = getSharedPreferences("step_counter_prefs", Context.MODE_PRIVATE)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        startForeground(NOTIFICATION_ID, createNotification())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        stepSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onSensorChanged(event: SensorEvent?) {
        val steps = event?.values?.get(0) ?: return
        val today = date
        val lastDate = prefs.getString("step_date", null)

        if (lastDate != today) {
            prefs.edit() {
                putString("step_date", today)
                    .putFloat("step_offset", steps)
            }
        }

        val offset = prefs.getFloat("step_offset", -1f)
        if (offset >= 0f) {
            val stepsToday = (steps - offset).toInt().coerceAtLeast(0)

            CoroutineScope(Dispatchers.IO).launch {
                dailyStepsDao.insert(DailyStepsEntity(date = today, steps = stepsToday))
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit

    private fun createNotification(): Notification {
        val channelId = "step_channel"
        val channelName = "Step Counter Service"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chan = NotificationChannel(
                channelId, channelName, NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(chan)
        }

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle(getString(R.string.steps_count))
            .setContentText(getString(R.string.step_counter_active))
            .setSmallIcon(R.drawable.ic_baseline_directions_walk_24)
            .build()
    }

    companion object {
        private const val NOTIFICATION_ID = 1
    }
}