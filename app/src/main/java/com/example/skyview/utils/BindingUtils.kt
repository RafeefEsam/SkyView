package com.example.skyview.utils

import android.annotation.SuppressLint
import android.content.ContentProvider
import android.content.Context
import android.util.Log
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.BindingAdapter
import com.example.skyview.R
import com.example.skyview.utils.enums.Temperature
import com.example.skyview.utils.enums.WindSpeed
import com.example.skyview.view.activities.HomeActivity
import com.example.skyview.view.activities.MainActivity
import com.example.skyview.view.fragments.HomeFragment
import pl.droidsonroids.gif.GifImageView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.log

private const val TAG = "BindingUtils"


@SuppressLint("SimpleDateFormat")
@BindingAdapter("hourFromDouble")
fun setHourFromDouble(view: TextView, value: Double) {
    val date = Date(value.toLong() * 1000)
    val sdf = SimpleDateFormat("hh:mm aa")
    val time1: String = sdf.format(date)
    view.text = time1
}

@BindingAdapter("dateFromLong")
fun setDateFromLong(view: TextView, timestamp: Long) {
    val dateFormat = SimpleDateFormat("E, MMM d", Locale.getDefault())
    val date = Date(timestamp)
    view.text = dateFormat.format(date)
}

@SuppressLint("SimpleDateFormat")
@BindingAdapter("dayFromDouble")
fun setDayFromDouble(view: TextView, value: Double) {
    val date = Date(value.toLong() * 1000)
    val sdf = SimpleDateFormat("EEEE")
    val dayName: String = sdf.format(date)
    view.text = dayName
}

@SuppressLint("SimpleDateFormat")
@BindingAdapter("fullDateFromDouble")
fun fullDateFromDouble(view: TextView, value: Double) {
    val date = Date(value.toLong() * 1000)
    val sdf = SimpleDateFormat("dd/M/yyyy hh:mm aa")
    val fullDate: String = sdf.format(date)
    view.text = fullDate
}

@BindingAdapter("imgFromString")
fun setImgFromString(view: GifImageView, value: String) {
    when (value) {
        "01d" -> view.setImageResource(R.drawable.sunny)
        "01n" -> view.setImageResource(R.drawable.moon)
        "02d", "02n" -> view.setImageResource(R.drawable.scatter_clouds)
        "03d", "03n" -> view.setImageResource(R.drawable.scatter_clouds)
        "04d", "04n" -> view.setImageResource(R.drawable.scatter_clouds)
        "09d", "09n" -> view.setImageResource(R.drawable.shower_rain)
        "10d", "10n" -> view.setImageResource(R.drawable.rain)
        "11d", "11n" -> view.setImageResource(R.drawable.thunder)
        "13d", "13n" -> view.setImageResource(R.drawable.snow)
        "50d", "50n" -> view.setImageResource(R.drawable.mist)
    }
}

@BindingAdapter("currentImgFromString")
fun setCurrentImgFromString(view: GifImageView, value: String?) {
    if (!value.isNullOrEmpty()) {
        when (value) {
            "01d" -> view.setImageResource(R.drawable.current_sun)
            "01n" -> view.setImageResource(R.drawable.moon)
            "02d", "02n" -> view.setImageResource(R.drawable.current_cloud)
            "03d", "03n" -> view.setImageResource(R.drawable.current_cloud)
            "04d", "04n" -> view.setImageResource(R.drawable.current_cloud)
            "09d", "09n" -> view.setImageResource(R.drawable.current_rain)
            "10d", "10n" -> view.setImageResource(R.drawable.current_rain)
            "11d", "11n" -> view.setImageResource(R.drawable.thunder)
            "13d", "13n" -> view.setImageResource(R.drawable.current_snow)
            "50d", "50n" -> view.setImageResource(R.drawable.mist)
        }
    } else {
        view.setImageResource(R.drawable.loading)
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("checkWindSpeed")
fun setWindSpeed(view: TextView, value: Double) {
    val context = view.tag as? Context
    if(context != null){
        if(SharedPreferencesClass(context).getRadioWindSpeed() == WindSpeed.meter_sec.toString()){
            view.text = "$value m/s"
        }else{
            var windSpeed = value * 2.23694
            view.text = "$windSpeed m/h"
        }
    }else{
        Log.e("setTemperature", "Context is null")

    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("checkTemperature", "app:context")
fun setTemperature(view: TextView, value: Double, context: Context) {
    when (SharedPreferencesClass(context).getRadioTemperature()) {
        Temperature.Celsius.toString() -> view.text = "${value.toInt()}º C "
        Temperature.Kelvin.toString() -> view.text = "${(value + 273.15).toInt()}º K "
        Temperature.Fahrenheit.toString() -> view.text = "${(value * (9 / 5) + 320).toInt()}º F "
    }
    view.tag = context
}





