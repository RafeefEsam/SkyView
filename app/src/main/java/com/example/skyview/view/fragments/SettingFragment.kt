package com.example.skyview.view.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.recreate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.skyview.R
import com.example.skyview.databinding.FragmentSettingBinding
import com.example.skyview.repository.LocationRepository
import com.example.skyview.utils.SharedPreferencesClass
import com.example.skyview.utils.enums.Language
import com.example.skyview.utils.enums.Location
import com.example.skyview.utils.enums.Temperature
import com.example.skyview.utils.enums.WindSpeed
import com.example.skyview.view.activities.MainActivity
import com.example.skyview.view.activities.MapsActivity
import com.example.skyview.viewmodel.viewmodel.LocationViewModel
import com.example.skyview.viewmodel.viewmodelfactory.LocationViewModelFactory
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.math.log

private const val TAG = "SettingFragment"

class SettingFragment : Fragment() {
    lateinit var settingFragmentBinding: FragmentSettingBinding
    private lateinit var sharedPreferencesClass: SharedPreferencesClass


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferencesClass = SharedPreferencesClass(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        settingFragmentBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_setting,
            container,
            false
        )
        return settingFragmentBinding.root
        // return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        expandButtons(view)
        selectRadioButton()

    }

    private fun expandButtons(view: View) {
        settingFragmentBinding.locationExpand.setOnClickListener {
            settingFragmentBinding.locationRadio.visibility = View.VISIBLE
        }
        settingFragmentBinding.tempExpand.setOnClickListener {
            settingFragmentBinding.tempRadio.visibility = View.VISIBLE
        }
        settingFragmentBinding.windExpand.setOnClickListener {
            settingFragmentBinding.windRadio.visibility = View.VISIBLE
        }
        settingFragmentBinding.lanExpand.setOnClickListener {
            settingFragmentBinding.lanRadio.visibility = View.VISIBLE
        }
    }

    private fun selectRadioButton() {
        settingFragmentBinding.locationRadio.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.gbs -> setGps()

                else -> setMap()
            }
        }
        settingFragmentBinding.tempRadio.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
//                R.id.celsius -> SharedPreferencesClass().setRadioTemperature(Temperature.Celsius)
//                R.id.kelvin -> SharedPreferencesClass().setRadioTemperature(Temperature.Kelvin)
//                else -> SharedPreferencesClass().setRadioTemperature(Temperature.Fahrenheit)
                R.id.celsius -> sharedPreferencesClass.setRadioTemperature(Temperature.Celsius)
                R.id.kelvin -> sharedPreferencesClass.setRadioTemperature(Temperature.Kelvin)
                else -> sharedPreferencesClass.setRadioTemperature(Temperature.Fahrenheit)
            }
        }
        settingFragmentBinding.windRadio.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.m_s -> SharedPreferencesClass(requireContext()).setRadioWindSpeed(WindSpeed.meter_sec)
                else -> SharedPreferencesClass(requireContext()).setRadioWindSpeed(WindSpeed.miles_hour)
            }
        }
        settingFragmentBinding.lanRadio.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.english -> SharedPreferencesClass(requireContext()).setRadioLanguage(Language.en)
                else -> SharedPreferencesClass(requireContext()).setRadioLanguage(Language.ar)
            }
            setLocale(SharedPreferencesClass(requireContext()).getRadioLanguage())
            recreate(requireActivity())

        }
    }
    private fun setGps(){
        SharedPreferencesClass(requireContext()).setRadioLocation(Location.GPS)
        lifecycleScope.launch(Dispatchers.IO) {
           var locationViewModelFactory  = LocationViewModelFactory(
                LocationRepository(
                    requireContext(),
                    LocationServices.getFusedLocationProviderClient(requireActivity()), MainActivity()
                )
            )
           var locationViewModel :LocationViewModel = ViewModelProvider(this@SettingFragment, locationViewModelFactory)
               .get(LocationViewModel::class.java)
            locationViewModel.getCurrentLatAndLon()
            Thread.sleep(3000)
            withContext(Dispatchers.Main) {
                locationViewModel.locationData.observe(viewLifecycleOwner, Observer {
                    SharedPreferencesClass(requireContext()).setLatitude(it.latitude)
                    SharedPreferencesClass(requireContext()).setLogitute(it.longitude)
                })
            }
        }
    }

    private fun setMap(){
        val intent = Intent(requireContext(), MapsActivity::class.java)
        startActivity(intent)
    }
    private fun setLocale(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources = resources
        val configuration = resources.configuration
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }
}