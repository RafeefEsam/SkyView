package com.example.skyview.model

import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.skyview.model.Alerts
import com.example.skyview.model.Current
import com.example.skyview.model.Daily
import com.example.skyview.model.Hourly
import com.example.skyview.utils.*
import java.util.TimeZone

@Entity(tableName = "weatherResponse")
@TypeConverters(
    CurrentTypeConverter::class,
    DailyTypeConverter::class,
    HourlyTypeConverter::class
)
data class MyResponse(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id:Int,
    @ColumnInfo(name = "timeZone")
    var timezone: String,
    @ColumnInfo(name = "current")
    val current: Current,
    @ColumnInfo(name = "daily")
    val daily: List<Daily>,
    @ColumnInfo(name = "hourly")
    val hourly: List<Hourly>,
    @ColumnInfo(name = "lat")
    val lat: Double,
    @ColumnInfo(name = "lon")
    val lon: Double

)