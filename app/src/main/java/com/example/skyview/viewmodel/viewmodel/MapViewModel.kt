package com.example.skyview.viewmodel.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.skyview.repository.MapRepository
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place

class MapViewModel(private val mapRepository: MapRepository) : ViewModel() {

    private var _currentLocation  = MutableLiveData<LatLng>()

    val currentLocation: LiveData<LatLng>
        get() = _currentLocation

    init {
        mapRepository.setUpMap()
        getCurrentLocationOnMap()
        mapRepository.searchWithCompletePlaces()
    }

     fun getCurrentLocationOnMap(){
        _currentLocation = mapRepository.getCurrentLocation()
        Log.i("TAGR", "getCurrentLocationOnMap:${_currentLocation.value} ")
    }

}