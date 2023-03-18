package com.example.skyview.viewmodel.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.skyview.repository.LocationRepository
import com.example.skyview.repository.MapRepository
import com.example.skyview.viewmodel.viewmodel.MapViewModel

class MapViewModelFactory (val mapRepository: MapRepository) :
    ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            MapViewModel(mapRepository) as T
        }
        else{
            throw java.lang.IllegalArgumentException("ViewModel not founded")
        }
    }

}