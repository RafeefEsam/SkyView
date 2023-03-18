package com.example.skyview.repository

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.skyview.model.LocationData
import com.example.skyview.utils.Constants
import com.example.skyview.view.activities.MainActivity
import com.google.android.gms.location.*
import kotlinx.coroutines.*
import java.util.*

private const val TAG = "LocationRepository"
class LocationRepository(val context: Context, var fusedLocationClient: FusedLocationProviderClient,
var activity: MainActivity) {

    private fun checkPermissions(): Boolean {
        Log.i(TAG, "checkPermissions: ")
        return ActivityCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun reguestPermission() {
        Log.i(TAG, "reguestPermission: ")
       // ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION,)
        ActivityCompat.requestPermissions(
         activity,
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            Constants.MY_PERMISSIONS_REQUEST_LOCATION
        )
    }


    @SuppressLint("MissingPermission")
     fun getCurrentLocation():MutableLiveData<LocationData> {
        Log.i(TAG, "getCurrentLocation: ")
        if (checkPermissions()) {
            if (checkLocationEnabled(context)){
                Log.i(TAG, "getCurrentLocation: ")
                return requestNewLocationData()
            }else{
                Toast.makeText(context, "Please turn on your location", Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(context, intent, null)
            }
        } else {
            reguestPermission()
           // return requestNewLocationData()
        }
        return requestNewLocationData()
    }

    fun checkLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    @SuppressLint("MissingPermission", "VisibleForTests")
    private fun requestNewLocationData(): MutableLiveData<LocationData> {
        Log.i(TAG, "requestNewLocationData: ")
        val locationLiveData = MutableLiveData<LocationData>()
        val locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        //fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val locationData = LocationData(location.latitude, location.longitude)
                Log.i(TAG, "requestNewLocationData:**** ${location.latitude}")
                locationLiveData.postValue(locationData)
            } else {
                // Handle location not available
            }
        }
        Log.i(TAG, "requestNewLocationData: ${locationLiveData.value?.latitude}")
        return locationLiveData

    }
}

