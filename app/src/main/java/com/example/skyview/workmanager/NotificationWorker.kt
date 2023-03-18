package com.example.skyview.workmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.skyview.R
import com.example.skyview.database.WeatherDataBase
import com.example.skyview.model.MyResponse
import com.example.skyview.utils.Constants.NOTIFICATION_CHANNEL_ID
import com.example.skyview.utils.Constants.NOTIFICATION_CHANNEL_NAME
import com.example.skyview.utils.Constants.NOTIFICATION_ID
import com.example.skyview.utils.SharedPreferencesClass
import com.example.skyview.utils.enums.Notification
import com.example.skyview.view.AlertWindow
import com.example.weatherapp.Networking.APIClient
import com.example.weatherapp.Networking.APIinterface
import com.example.weatherforcast.model.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "NotificationWorker"

class NotificationWorker(var context: Context, params: WorkerParameters) :
    Worker(context, params) {
    lateinit var weatherRepository: WeatherRepository
    var weatherList: Flow<MyResponse> = flowOf()
    var weatherData: MyResponse? = null
    override fun doWork(): Result {
        weatherRepository = WeatherRepository(
            WeatherDataBase.getDatabase(context).weatherDao(),
            APIClient.retrofitInstnce.create(APIinterface::class.java)
            ,WeatherDataBase.getDatabase(context).localDao())
        GlobalScope.launch(Dispatchers.IO) {
            weatherList = weatherRepository.getCurrentWeather(
                SharedPreferencesClass(context).getLatitude(),
                SharedPreferencesClass(context).getLongtitude(),
                SharedPreferencesClass(context).getRadioLanguage()
            )
            weatherList.collect {
                weatherData = it
            }
            Log.i(TAG, "doWork: *******************io")
            withContext(Dispatchers.Main){
                if (!Settings.canDrawOverlays(context)) {
                    val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                    intent.data = Uri.parse("package:" + context.packageName)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                } else {
                    Log.i(TAG, "doWork: else ++++++++++++++++++++")
                    when (SharedPreferencesClass(context).getNotification()) {
                        Notification.noti.toString() -> showNotification("notification",
                            "${weatherData?.daily?.get(0)?.weather?.get(0)?.description}")
                        else -> GlobalScope.launch(Dispatchers.Main) {
                            AlertWindow(context).show("alarm",
                                "${weatherData?.daily?.get(0)?.weather?.get(0)?.description}")
                        }

                    }
                }
            }
        }


        // showNotification("hello", "this is rafeef")

        return Result.success()
    }

    private fun showNotification(title: String, description: String) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationChannel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(notificationChannel)

        val notificationBuilder =
            NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.app_iocn)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
        val notification = notificationBuilder.build()
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}