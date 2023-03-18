package com.example.skyview.view.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.skyview.R
import com.example.skyview.database.WeatherDataBase
import com.example.skyview.model.LocationData
import com.example.skyview.repository.LocationRepository
import com.example.skyview.utils.Constants
import com.example.skyview.utils.SharedPreferencesClass
import com.example.skyview.utils.enums.Language
import com.example.skyview.utils.enums.Location
import com.example.skyview.utils.enums.Temperature
import com.example.skyview.utils.enums.WindSpeed
import com.example.skyview.viewmodel.viewmodel.WeatherViewModel
import com.example.skyview.viewmodel.viewmodel.LocationViewModel
import com.example.skyview.viewmodel.viewmodelfactory.WeatherViewModelFactory
import com.example.skyview.viewmodel.viewmodelfactory.LocationViewModelFactory
import com.example.weatherapp.Networking.APIClient
import com.example.weatherapp.Networking.APIinterface
import com.example.weatherforcast.model.WeatherRepository
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "MainActivity1"

class MainActivity : AppCompatActivity() {
    private lateinit var homeviewModel: WeatherViewModel
    private lateinit var hViewModelFactory: WeatherViewModelFactory
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var locationViewModelFactory: LocationViewModelFactory
    private lateinit var locationData: LocationData
    private lateinit var sharedPreferencesClass: SharedPreferencesClass

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init(this)
        setIntSharedPref()

        lifecycleScope.launch(Dispatchers.Main) {
            if (!checkPermissions()) {
                homeviewModel.getUserChoise(this@MainActivity)
            } else {
                startActivity(Intent(this@MainActivity, HomeActivity::class.java))
            }
            locationData = LocationData(9.0, 3.5)
        }

        homeviewModel.locationPreference.observe(this, Observer { preference ->
            when (preference) {
                "Location" -> {
                    // Do something with the user's location
                    lifecycleScope.launch(Dispatchers.IO) {
                        locationViewModel.getCurrentLatAndLon()
                        Log.i(TAG, "onCreate---: ${locationViewModel.locationData.value}")
                        Thread.sleep(3000)
                        withContext(Dispatchers.Main) {
                            Log.i(TAG, "onCreate: main")
                            locationViewModel.locationData.observe(this@MainActivity, Observer {
                                Log.i(TAG, "onCreate: LAt ${it.latitude}")
                                locationData = it
                                sharedPreferencesClass.setLatitude(it.latitude)
                                sharedPreferencesClass.setLogitute(it.longitude)
                            })
                            val intent = Intent(this@MainActivity, HomeActivity::class.java)
                            intent.putExtra(Constants.PASSED_LOCATION_DATA, locationData)
                            startActivity(intent)
                        }
                    }
                }
                "Map" -> {
                    val intent = Intent(this@MainActivity, MapsActivity::class.java)
                    startActivity(intent)
                }
                else -> {
                    Toast.makeText(
                        this, "you need to give permission",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationViewModel.getCurrentLatAndLon()
            }
        }
    }

    private fun setIntSharedPref() {
        sharedPreferencesClass.setRadioLanguage(Language.en)
        sharedPreferencesClass.setRadioLocation(Location.GPS)
        sharedPreferencesClass.setRadioTemperature(Temperature.Celsius)
        sharedPreferencesClass.setRadioWindSpeed(WindSpeed.meter_sec)
    }

    private fun init(context: Context) {
        sharedPreferencesClass = SharedPreferencesClass(context)
        hViewModelFactory = WeatherViewModelFactory(
            WeatherRepository(
                WeatherDataBase.getDatabase(context).weatherDao(),
                APIClient.retrofitInstnce.create(APIinterface::class.java)
                ,WeatherDataBase.getDatabase(this).localDao()
            )
        )
        homeviewModel =
            ViewModelProvider(this, hViewModelFactory).get(WeatherViewModel::class.java)
        locationViewModelFactory = LocationViewModelFactory(
            LocationRepository(
                this,
                LocationServices.getFusedLocationProviderClient(this), this
            )
        )
        locationViewModel =
            ViewModelProvider(this, locationViewModelFactory)[LocationViewModel::class.java]
    }

}