package com.example.skyview.repository

import android.content.Context
import com.example.skyview.database.AlarmDAO
import com.example.skyview.database.WeatherDataBase
import com.example.skyview.model.AlarmPojo
import kotlinx.coroutines.flow.Flow

class AlarmRepository(context: Context) : AlarmDAO{
    private var alarmDAO: AlarmDAO = WeatherDataBase.getDatabase(context).alarmDAO()

    override fun getAllAlarmsList(): Flow<List<AlarmPojo>> {
        return alarmDAO.getAllAlarmsList()
    }

    override fun insertAlarm(alarmPojo: AlarmPojo) {
        alarmDAO.insertAlarm(alarmPojo)
    }

    override fun deleteAlarm(alarmPojo: AlarmPojo) {
        alarmDAO.deleteAlarm(alarmPojo)
    }

}