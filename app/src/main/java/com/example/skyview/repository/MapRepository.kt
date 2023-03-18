package com.example.skyview.repository

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import com.example.skyview.R
import com.example.skyview.view.activities.MapsActivity
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener

class MapRepository(val context: Context, val mMap: GoogleMap, val activity: MapsActivity) {
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    val MY_PERMISSIONS_REQUEST_LOCATION = 100
    private var currentMarker: Marker? = null
    companion object{
        lateinit var currentLatLng: MutableLiveData<LatLng>

    }

     fun setUpMap(){
         currentLatLng =
             MutableLiveData(LatLng(30.4,31.5))
        if (ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                MY_PERMISSIONS_REQUEST_LOCATION
            )
                return
        }
        mMap.isMyLocationEnabled = true
         fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        fusedLocationClient.lastLocation.addOnSuccessListener(activity) {
            if (it != null){
                lastLocation = it
                currentLatLng =
                  MutableLiveData(LatLng(it.latitude,it.longitude))

              //  var currentLatLng :LiveData<LatLng> = LatLng(it.latitude, it.longitude) as liv
                placeMarkerInMap(currentLatLng)
                currentLatLng.value?.let { it1 ->
                    CameraUpdateFactory.newLatLngZoom(
                        it1, 12f)
                }?.let { it2 -> mMap.animateCamera(it2) }
            }

        }
    }

     fun placeMarkerInMap(latLng: MutableLiveData<LatLng>){
         currentLatLng = latLng
        currentMarker =  mMap.addMarker(
            MarkerOptions()
            .position(latLng.value!!).title("${latLng.value}"))

    }

    fun getCurrentLocation():MutableLiveData<LatLng>{
        return currentLatLng
    }

     fun searchWithCompletePlaces(){
        val autocompleteFragment =
            activity.supportFragmentManager.findFragmentById(R.id.autocomplete_fragment) as
                    AutocompleteSupportFragment

        autocompleteFragment.setPlaceFields(
            listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG
            )
        )

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // Move the map to the selected place
                val latLng = MutableLiveData(place.latLng)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng.value!!, 15f))
                currentMarker?.remove()
                placeMarkerInMap(latLng)
            }

            override fun onError(status: Status) {
                Log.e("TAG", "onError: ${status.statusMessage}")
            }
        })
    }

    fun onMapClick(){
            mMap.setOnMapClickListener {
                currentMarker?.remove()
            placeMarkerInMap(MutableLiveData(it))
        }
    }

}