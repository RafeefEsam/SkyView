package com.example.skyview.view.activities

import android.content.Intent
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.example.skyview.BuildConfig.MAPS_API_KEY
import com.example.skyview.R
import com.example.skyview.database.WeatherDataBase
import com.example.skyview.databinding.ActivityMapsBinding
import com.example.skyview.model.FavoriteModel
import com.example.skyview.model.LocationData
import com.example.skyview.repository.MapRepository
import com.example.skyview.utils.Constants
import com.example.skyview.utils.SharedPreferencesClass
import com.example.skyview.viewmodel.viewmodel.MapViewModel
import com.example.skyview.viewmodel.viewmodel.WeatherViewModel
import com.example.skyview.viewmodel.viewmodelfactory.MapViewModelFactory
import com.example.skyview.viewmodel.viewmodelfactory.WeatherViewModelFactory
import com.example.weatherapp.Networking.APIClient
import com.example.weatherapp.Networking.APIinterface
import com.example.weatherforcast.model.WeatherRepository
import com.google.android.gms.maps.model.Marker
import com.google.android.libraries.places.api.Places
import kotlinx.coroutines.launch

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var mapRepository: MapRepository
    private lateinit var mapViewModel: MapViewModel
    private lateinit var mapViewModelFactory: MapViewModelFactory
    private lateinit var latLan: LocationData
    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var weatherViewModelFactory: WeatherViewModelFactory
    private lateinit var sharedPreferencesClass: SharedPreferencesClass
    var address : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferencesClass = SharedPreferencesClass(this)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
      //  fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if(!Places.isInitialized()){
            Places.initialize(applicationContext, MAPS_API_KEY)
        }
        weatherViewModelFactory = WeatherViewModelFactory(WeatherRepository(WeatherDataBase.getDatabase(this).weatherDao()
            , APIClient.retrofitInstnce.create(APIinterface::class.java)
            ,WeatherDataBase.getDatabase(this).localDao()))
        weatherViewModel =
            ViewModelProvider(this, weatherViewModelFactory).get(WeatherViewModel::class.java)
        latLan = LocationData(3.4, 3.5)

        binding.fav.setOnClickListener {
            lifecycleScope.launch {
                weatherViewModel.insetToFav(FavoriteModel(String.format("%.4f", latLan.latitude).toDouble()
                    , String.format("%.4f", latLan.longitude).toDouble()))
            }
            Toast.makeText(this@MapsActivity, "Insert to fav successfully",
                Toast.LENGTH_SHORT).show()
        }
        binding.weather.setOnClickListener {
            sharedPreferencesClass.setLatitude(latLan.latitude)
            sharedPreferencesClass.setLogitute(latLan.longitude)
            val intent = Intent(this@MapsActivity, HomeActivity::class.java)
            intent.putExtra(Constants.PASSED_LOCATION_DATA, latLan)
            startActivity(intent)
        }


    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)
        mapRepository = MapRepository(applicationContext, mMap, this)
        mapViewModelFactory = MapViewModelFactory(mapRepository)
        mapViewModel =
            ViewModelProvider(this, mapViewModelFactory).get(MapViewModel::class.java)

        mapRepository.onMapClick()
    }
    override fun onMarkerClick(p0: Marker): Boolean {
        mapViewModel.getCurrentLocationOnMap()
        mapViewModel.currentLocation.observe(this){
            latLan = LocationData(it.latitude, it.longitude)
            val geocoder = Geocoder(this)
            val addresses = geocoder.getFromLocation(it.latitude, it.longitude, 1)
            if (addresses?.isNotEmpty() == true) {
                address = addresses[0].getAddressLine(0)
                binding.address.text = address
            }
            binding.mapActionCard.visibility = VISIBLE
        }
        return false
    }


}