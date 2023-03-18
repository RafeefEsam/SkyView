package com.example.skyview.viewmodel.viewmodel

import androidx.lifecycle.*
import com.example.skyview.database.AlarmDAO
import com.example.skyview.model.AlarmPojo
import com.example.skyview.model.LocationData
import com.example.skyview.repository.AlarmRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmViewModel(
    var repository: AlarmDAO
):ViewModel() {

    private var _alarmData = MutableLiveData<List<AlarmPojo>>()
    val alarmData: LiveData<List<AlarmPojo>>
        get() = _alarmData

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getAllAlarms()

        }
    }


    suspend fun getAllAlarms(){
        repository.getAllAlarmsList().collect{
            _alarmData.postValue(it)
        }
    }

    fun addAlarm(alarmPojo: AlarmPojo){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertAlarm(alarmPojo)
        }
    }

    fun deleteAlarm(alarmPojo: AlarmPojo){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAlarm(alarmPojo)
        }
    }
}