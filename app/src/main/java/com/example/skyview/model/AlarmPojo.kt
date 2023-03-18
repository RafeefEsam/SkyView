package com.example.skyview.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import java.io.Serializable

@Entity(tableName = "alarm")
data class AlarmPojo(
    var alarmTime : Long,
    var isNotification : Boolean
) : Serializable{
    @PrimaryKey(autoGenerate = true)
    @NotNull
    var id : Int = 0
}
