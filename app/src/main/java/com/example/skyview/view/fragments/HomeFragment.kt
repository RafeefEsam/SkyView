package com.example.skyview.view.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.skyview.R
import com.example.skyview.database.WeatherDataBase
import com.example.skyview.databinding.FragmentHomeBinding
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
import kotlinx.coroutines.launch

private const val TAG = "HomeFragment"

class HomeFragment : Fragment() {
    lateinit var weatherViewModel: WeatherViewModel
    lateinit var weatherViewModelFactory: WeatherViewModelFactory
    lateinit var binding: FragmentHomeBinding
    lateinit var hourlyRecyclerAdapter: HourlyRecyclerAdapter
    lateinit var dailyRecyclerAdapter: DailyRecyclerAdapter
    lateinit var weatherData: LocationData
    lateinit var sharedPreferencesClass: SharedPreferencesClass
    //private val connectivityManager = requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferencesClass = SharedPreferencesClass(requireContext())
        val intent = requireActivity().intent
        weatherData = intent.getParcelableExtra(Constants.PASSED_LOCATION_DATA)
            ?: LocationData(sharedPreferencesClass.getLatitude(),
                sharedPreferencesClass.getLongtitude())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        weatherViewModelFactory = WeatherViewModelFactory(WeatherRepository(
            WeatherDataBase.getDatabase(requireContext()).weatherDao(),
            APIClient.retrofitInstnce.create(APIinterface::class.java),
            WeatherDataBase.getDatabase(requireContext()).localDao()))
        weatherViewModel =
            ViewModelProvider(this, weatherViewModelFactory).get(WeatherViewModel::class.java)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home,
            container,
            false
        )
        weatherViewModel.getCurrentWeatherFromAPI(weatherData.latitude, weatherData.longitude,
            lang = SharedPreferencesClass(requireContext()).getRadioLanguage())

        binding.lifecycleOwner = this
        hourlyRecyclerAdapter = HourlyRecyclerAdapter(listOf())
        binding.hourlyAdapter = hourlyRecyclerAdapter
        dailyRecyclerAdapter = DailyRecyclerAdapter(listOf(), requireContext())
        binding.dailyAdapter = dailyRecyclerAdapter
        return binding.root
        //  return inflater.inflate(R.layout.fragment_home, container, false)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isInternetAvailable(requireContext())) {
            lifecycleScope.launchWhenCreated {
                weatherViewModel.data.collect {
                    when (it) {
                        is ApiState.Success -> {
                            Log.i(TAG, "onCreateView:  ${it.data.hourly.size}")
                            weatherViewModel.insertLocalWeather(it.data)
                            binding.tvOffline.visibility = GONE
                            binding.lodingImg.visibility = GONE
                            binding.homeScroll.visibility = VISIBLE
                            hourlyRecyclerAdapter.hourly = it.data.hourly
                            hourlyRecyclerAdapter.notifyDataSetChanged()
                            dailyRecyclerAdapter.daily = it.data.daily
                            dailyRecyclerAdapter.notifyDataSetChanged()
                            binding.response = it.data
                        }
                        is ApiState.Failure -> {
                            Toast.makeText(requireContext(), "${it.msg}", Toast.LENGTH_LONG).show()
                        }
                        else -> {
                            Log.i(TAG, "onCreateView: else")
                        }
                    }
                }
            }
        }else{
            lifecycleScope.launch {
                weatherViewModel.getLastLocalWeather().collect{
                    binding.tvOffline.visibility = VISIBLE
                    binding.homeScroll.visibility = VISIBLE
                    binding.lodingImg.visibility = GONE
                    hourlyRecyclerAdapter.hourly = it.hourly
                    hourlyRecyclerAdapter.notifyDataSetChanged()
                    dailyRecyclerAdapter.daily = it.daily
                    dailyRecyclerAdapter.notifyDataSetChanged()
                    binding.response = it
                }
            }

        }
    }

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}