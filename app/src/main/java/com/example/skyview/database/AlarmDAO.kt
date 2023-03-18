package com.example.skyview.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.skyview.model.AlarmPojo
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDAO {

    @Query("SELECT * FROM alarm")
    fun getAllAlarmsList() : Flow<List<AlarmPojo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAlarm(alarmPojo: AlarmPojo)

    @Delete
    fun deleteAlarm(alarmPojo: AlarmPojo)


}