package com.example.skyview.view.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.skyview.R
import com.example.skyview.database.WeatherDataBase
import com.example.skyview.databinding.FragmentFavouriteBinding
import com.example.skyview.model.ApiState
import com.example.skyview.model.MyResponse
import com.example.skyview.utils.Constants
import com.example.skyview.utils.SharedPreferencesClass
import com.example.skyview.view.activities.FavouriteActivity
import com.example.skyview.view.activities.MapsActivity
import com.example.skyview.view.adapters.FavouriteRecyclerAdapter
import com.example.skyview.viewmodel.viewmodel.WeatherViewModel
import com.example.skyview.viewmodel.viewmodelfactory.WeatherViewModelFactory
import com.example.weatherapp.Networking.APIClient
import com.example.weatherapp.Networking.APIinterface
import com.example.weatherforcast.model.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "FavouriteFragment"

class FavouriteFragment : Fragment() {

    lateinit var weatherViewModel: WeatherViewModel
    lateinit var weatherViewModelFactory: WeatherViewModelFactory
    lateinit var favouriteRecyclerAdapter: FavouriteRecyclerAdapter
    lateinit var fragmentFavouriteBinding: FragmentFavouriteBinding
    var favList: List<MyResponse> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate: ")
        super.onCreate(savedInstanceState)
        weatherViewModelFactory = WeatherViewModelFactory(WeatherRepository(
            WeatherDataBase.getDatabase(requireContext()).weatherDao(),
            APIClient.retrofitInstnce.create(APIinterface::class.java)
            ,WeatherDataBase.getDatabase(requireContext()).localDao()))
        weatherViewModel =
            ViewModelProvider(this, weatherViewModelFactory).get(WeatherViewModel::class.java)

        // weatherViewModel.getFavWeather()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentFavouriteBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_favourite,
            container,
            false
        )
        fragmentFavouriteBinding.lifecycleOwner = this
        favouriteRecyclerAdapter = FavouriteRecyclerAdapter(listOf() , { lat, lon ->
            lifecycleScope.launch(Dispatchers.IO) {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Delete Item")
                builder.setMessage("Are you sure you want to delete this item?")
                builder.setPositiveButton("Yes") { dialog, which ->
                    // User clicked Yes, perform deletion
                    weatherViewModel.deletebyLatLon(lat, lon)
                }
                builder.setNegativeButton("No", null)
                withContext(Dispatchers.Main) {
                    val alert = builder.create()
                    alert.show()
                    fragmentFavouriteBinding.favRecycler.adapter?.notifyDataSetChanged()
                }
            }


        },{
            val intent = Intent(requireContext(), FavouriteActivity::class.java)
            intent.putExtra(Constants.PASSED_LOCATION_DATA, it)
            startActivity(intent)

        })
        fragmentFavouriteBinding.responseAdapter = favouriteRecyclerAdapter

        // Inflate the layout for this fragment
        //  return inflater.inflate(R.layout.fragment_favourite, container, false)
        return fragmentFavouriteBinding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherViewModel.favWeather.observe(viewLifecycleOwner, Observer {
            for (i in it.indices) {
                lifecycleScope.launch(Dispatchers.IO) {
                    weatherViewModel.getCurrentWeatherFromAPI(it.get(i).lat, it.get(i).lon,
                        SharedPreferencesClass(requireContext()).getRadioLanguage())
                }
            }
            lifecycleScope.launch(Dispatchers.IO) {
                weatherViewModel.data.collect {
                    when (it) {
                        is ApiState.Success -> {
                            favList += it.data
                            favList = favList.distinct()
                            Log.i(TAG, "onViewCreated: after add ${favList.size}")
                        }
                        is ApiState.Failure -> {
                            withContext(Dispatchers.IO) {
                                Toast.makeText(requireContext(), "${it.msg}", Toast.LENGTH_LONG)
                                    .show()
                            }
                        }
                        else -> {
                            Log.i(TAG, "onCreateView: else")
                        }
                    }
                    withContext(Dispatchers.Main) {
                        favouriteRecyclerAdapter.myResponseList = favList
                        favouriteRecyclerAdapter.notifyDataSetChanged()
                    }
                }

            }
        })
        fragmentFavouriteBinding.addFavbtn.setOnClickListener {
            val intent = Intent(requireContext(), MapsActivity::class.java)
            startActivity(intent)
        }
        fragmentFavouriteBinding.favRecycler.adapter?.notifyDataSetChanged()

    }
}

