package com.example.skyview.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.view.ViewConfiguration.get
import com.example.skyview.utils.enums.*
import kotlin.properties.Delegates

class SharedPreferencesClass(context: Context) {
    val fileName: String = "skyView"
    var sharedPreferences: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null

    init {
        initSharedPrefrences(context)
    }

    fun initSharedPrefrences(context: Context) {
        sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
        editor = sharedPreferences?.edit()

    }

    @SuppressLint("CommitPrefEdits")
    fun setLatitude(latitude: Double) {
        editor?.putFloat(Constants.SHARED_LATITUDE, latitude.toFloat())
        editor?.commit()
    }

    @SuppressLint("CommitPrefEdits")
    fun setLogitute(longitute: Double) {
        editor?.putFloat(Constants.SHARED_LONGTITUDE, longitute.toFloat())
        editor?.commit()
    }

    fun getLatitude(): Double {
        return sharedPreferences?.getFloat(Constants.SHARED_LATITUDE, 0f)?.toDouble() ?: 0.0
    }

    fun getLongtitude(): Double {
        return sharedPreferences?.getFloat(Constants.SHARED_LONGTITUDE, 0f)?.toDouble() ?: 0.0
    }
    fun setRadioLocation(location: Location){
        editor?.putString(Constants.SHARED_LOCATION, location.toString())
        editor?.commit()
    }
    fun getRadioLocation():String{
        return sharedPreferences?.getString(Constants.SHARED_LOCATION,Location.GPS.toString())
            ?: Location.GPS.toString()
    }
    fun setRadioTemperature(temperature: Temperature){
        editor?.putString(Constants.SHARED_TEMPERATURE, temperature.toString())
        editor?.commit()
    }
    fun getRadioTemperature():String{
        return sharedPreferences?.getString(Constants.SHARED_TEMPERATURE,Temperature.Celsius.toString())
            ?: Temperature.Celsius.toString()
    }
    fun setRadioLanguage(language: Language){
        editor?.putString(Constants.SHARED_LANGUAGE, language.toString())
        editor?.commit()
    }
    fun getRadioLanguage():String{
        return sharedPreferences?.getString(Constants.SHARED_LANGUAGE,Language.en.toString())
            ?: Language.en.toString()
    }
    fun setRadioWindSpeed(windSpeed: WindSpeed){
        editor?.putString(Constants.SHARED_WIND_SPEED, windSpeed.toString())
        editor?.commit()
    }
    fun getRadioWindSpeed():String{
        return sharedPreferences?.getString(Constants.SHARED_WIND_SPEED,WindSpeed.meter_sec.toString())
            ?: WindSpeed.meter_sec.toString()
    }
    fun setNotification(notification: Notification){
        editor?.putString(Constants.SHARED_NOTIFICATION, notification.toString())
        editor?.commit()
    }
    fun getNotification():String{
        return sharedPreferences?.getString(Constants.SHARED_NOTIFICATION, Notification.alarm.toString())
            ?:Notification.alarm.toString()
    }



}