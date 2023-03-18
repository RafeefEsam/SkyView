package com.example.skyview.view.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.skyview.database.WeatherDataBase
import com.example.skyview.databinding.ActivityFavouriteBinding
import com.example.skyview.model.ApiState
import com.example.skyview.model.LocationData
import com.example.skyview.utils.Constants
import com.example.skyview.utils.SharedPreferencesClass
import com.example.skyview.view.adapters.DailyRecyclerAdapter
import com.example.skyview.view.adapters.HourlyRecyclerAdapter
import com.example.skyview.viewmodel.viewmodel.WeatherViewModel
import com.example.skyview.viewmodel.viewmodelfactory.WeatherViewModelFactory
import com.example.weatherapp.Networking.APIClient
import com.example.weatherapp.Networking.APIinterface
import com.example.weatherforcast.model.WeatherRepository

class FavouriteActivity : AppCompatActivity() {
    lateinit var weatherData: LocationData
    lateinit var weatherViewModel: WeatherViewModel
    lateinit var weatherViewModelFactory: WeatherViewModelFactory
    lateinit var binding: ActivityFavouriteBinding
    lateinit var hourlyRecyclerAdapter: HourlyRecyclerAdapter
    lateinit var dailyRecyclerAdapter: DailyRecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = this.intent
        weatherData = intent.getParcelableExtra(Constants.PASSED_LOCATION_DATA)!!
        binding = ActivityFavouriteBinding.inflate(layoutInflater)
        weatherViewModelFactory = WeatherViewModelFactory(
            WeatherRepository(
            WeatherDataBase.getDatabase(this).weatherDao(),
            APIClient.retrofitInstnce.create(APIinterface::class.java)
                ,WeatherDataBase.getDatabase(this).localDao())
        )
        weatherViewModel =
            ViewModelProvider(this, weatherViewModelFactory).get(WeatherViewModel::class.java)
        weatherViewModel.getCurrentWeatherFromAPI(weatherData.latitude, weatherData.longitude,
        SharedPreferencesClass(this).getRadioLanguage())
        binding.lifecycleOwner = this
        hourlyRecyclerAdapter = HourlyRecyclerAdapter(listOf())
        binding.hourlyAdapter = hourlyRecyclerAdapter
        dailyRecyclerAdapter = DailyRecyclerAdapter(listOf(), this)
        binding.dailyAdapter = dailyRecyclerAdapter
        getData()
        setContentView(binding.root)

    }

    @SuppressLint("NotifyDataSetChanged")
    fun getData(){
        lifecycleScope.launchWhenCreated {
            weatherViewModel.data.collect {
                when (it) {
                    is ApiState.Success -> {
                        binding.lodingImg.visibility = View.GONE
                        binding.homeScroll.visibility = View.VISIBLE
                        hourlyRecyclerAdapter.hourly = it.data.hourly
                        hourlyRecyclerAdapter.notifyDataSetChanged()
                        dailyRecyclerAdapter.daily = it.data.daily
                        dailyRecyclerAdapter.notifyDataSetChanged()
                        binding.response = it.data
                    }
                    is ApiState.Failure -> {
                        Toast.makeText(FavouriteActivity(), "${it.msg}", Toast.LENGTH_LONG).show()
                    }
                    else -> {
                    }
                }
            }
        }
    }
}