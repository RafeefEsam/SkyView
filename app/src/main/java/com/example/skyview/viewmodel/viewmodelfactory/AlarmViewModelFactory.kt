package com.example.skyview.viewmodel.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.skyview.repository.AlarmRepository
import com.example.skyview.viewmodel.viewmodel.AlarmViewModel
import java.lang.IllegalArgumentException

class AlarmViewModelFactory(
    private val _repository: AlarmRepository
) :ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(AlarmViewModel::class.java)){
            AlarmViewModel(_repository) as T
        }else{
            throw IllegalArgumentException("There is No View Model Class Found")
        }
    }
}