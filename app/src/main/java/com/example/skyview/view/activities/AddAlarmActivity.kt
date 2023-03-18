package com.example.skyview.view.activities

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import com.example.skyview.databinding.ActivityAddAlarmBinding
import com.example.skyview.model.AlarmPojo
import com.example.skyview.repository.AlarmRepository
import com.example.skyview.utils.Constants
import com.example.skyview.utils.SharedPreferencesClass
import com.example.skyview.utils.enums.Notification
import com.example.skyview.viewmodel.viewmodel.AlarmViewModel
import com.example.skyview.viewmodel.viewmodel.MapViewModel
import com.example.skyview.viewmodel.viewmodelfactory.AlarmViewModelFactory
import com.example.skyview.viewmodel.viewmodelfactory.MapViewModelFactory
import com.example.skyview.workmanager.NotificationBroadCast
import java.util.*

private const val TAG = "AddAlarmActivity"
class AddAlarmActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddAlarmBinding
    lateinit var alarmViewModel: AlarmViewModel
    lateinit var alarmFactory: AlarmViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        alarmFactory = AlarmViewModelFactory(AlarmRepository(this))
        alarmViewModel =
            ViewModelProvider(this, alarmFactory).get(AlarmViewModel::class.java)
        createNotificationChannel()
        binding.notificationButton.setOnClickListener{
            SharedPreferencesClass(this).setNotification(Notification.noti)
            scheduleNotification()
            alarmViewModel.addAlarm(AlarmPojo(getTime(), true))
        }
        binding.alarmButton.setOnClickListener {
            SharedPreferencesClass(this).setNotification(Notification.alarm)
            scheduleNotification()
            alarmViewModel.addAlarm(AlarmPojo(getTime(), false))
        }
    }

    private fun scheduleNotification() {
        val intent = Intent(applicationContext,NotificationBroadCast::class.java )
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            Constants.NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = getTime()
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
        showAlert(time)
    }

    private fun showAlert(time: Long) {
        val date = Date(time)
        val dateFormat = android.text.format.DateFormat.getLongDateFormat(applicationContext)
        val timeFormat = android.text.format.DateFormat.getTimeFormat(applicationContext)

        AlertDialog.Builder(this)
            .setTitle("Notification Scheduled")
            .setMessage(
                "\nAt: " + dateFormat.format(date) + " " + timeFormat.format(date)
            )
            .setPositiveButton("Okay") { _, _ -> }
            .show()
    }

    private fun getTime(): Long {
        val minute = binding.timePicker.minute
        val hour = binding.timePicker.hour
        val day = binding.datePicker.dayOfMonth
        val month = binding.datePicker.month
        val year = binding.datePicker.year
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, hour, minute)
        return calendar.timeInMillis
    }

    private fun createNotificationChannel() {
        val name = "Notif Channel"
        val desc = "A Description of the Channel"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(Constants.NOTIFICATION_CHANNEL_ID, name, importance)
        channel.description = desc
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

}