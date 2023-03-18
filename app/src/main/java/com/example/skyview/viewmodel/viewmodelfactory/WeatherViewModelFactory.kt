package com.example.skyview.viewmodel.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.skyview.viewmodel.viewmodel.WeatherViewModel
import com.example.weatherforcast.model.WeatherRepositoryInterface

class WeatherViewModelFactory (val weatherRepository: WeatherRepositoryInterface) :
    ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            WeatherViewModel(weatherRepository) as T
        }
        else{
            throw java.lang.IllegalArgumentException("ViewModel not founded")
        }


    }
}