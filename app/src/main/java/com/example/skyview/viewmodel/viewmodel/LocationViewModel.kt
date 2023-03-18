package com.example.skyview.viewmodel.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.skyview.model.LocationData
import com.example.skyview.repository.LocationRepository

class LocationViewModel(private val locationRepository: LocationRepository): ViewModel() {

    private var _locationData = MutableLiveData<LocationData>()
    val locationData: LiveData<LocationData>
        get() = _locationData
//    init {
//        getCurrentLatAndLon()
//    }
   fun getCurrentLatAndLon(){
        _locationData = locationRepository.getCurrentLocation()
    }

}