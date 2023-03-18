package com.example.skyview.viewmodel.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.skyview.repository.LocationRepository
import com.example.skyview.viewmodel.viewmodel.LocationViewModel

class LocationViewModelFactory (val locationRepository: LocationRepository) :
    ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LocationViewModel::class.java)) {
            LocationViewModel(locationRepository) as T
        }
        else{
            throw java.lang.IllegalArgumentException("ViewModel not founded")
        }
    }
}