package com.example.skyview.utils

import androidx.room.TypeConverter
import com.example.skyview.model.Alerts
import com.example.skyview.model.Current
import com.example.skyview.model.Daily
import com.example.skyview.model.Hourly
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CurrentTypeConverter {
    @TypeConverter
    fun fromJson(json: String): Current {
        return Gson().fromJson(json, Current::class.java)
    }

    @TypeConverter
    fun toJson(current: Current): String {
        return Gson().toJson(current)
    }
}

class DailyTypeConverter {
    @TypeConverter
    fun fromJson(json: String): List<Daily> {
        val type = object : TypeToken<List<Daily>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toJson(dailyList: List<Daily>): String {
        return Gson().toJson(dailyList)
    }
}

class HourlyTypeConverter {
    @TypeConverter
    fun fromJson(json: String): List<Hourly> {
        val type = object : TypeToken<List<Hourly>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toJson(hourlyList: List<Hourly>): String {
        return Gson().toJson(hourlyList)
    }
}

