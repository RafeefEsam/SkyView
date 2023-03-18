package com.example.skyview.viewmodel.viewmodel

import android.content.Context
import androidx.lifecycle.*
import com.example.skyview.model.ApiState
import com.example.skyview.model.FavoriteModel
import com.example.skyview.model.MyResponse
import com.example.weatherforcast.model.WeatherRepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class WeatherViewModel(private val weatherRepository: WeatherRepositoryInterface) : ViewModel() {

    private var _data = MutableStateFlow<ApiState>(ApiState.Loading)
    var data = _data.asStateFlow()

    private val _locationPreference = MutableLiveData<String>()

    val locationPreference: LiveData<String>
        get() = _locationPreference
    val favWeather: LiveData<List<FavoriteModel>>
        get() = weatherRepository.getAllFavWeather().asLiveData()


    fun getCurrentWeatherFromAPI(lat: Double, lon: Double, lang: String) = viewModelScope.launch(Dispatchers.IO) {
        weatherRepository.getCurrentWeather(lat, lon, lang = lang).catch { e ->
            _data.value = ApiState.Failure(e)
        }.collect { data ->
            _data.value = ApiState.Success(data)
        }
    }


    suspend fun getUserChoise(context: Context) {
        val preference = weatherRepository.getUserChoise(context)
        _locationPreference.postValue(preference)
    }

    suspend fun insetToFav(favoriteModel: FavoriteModel) {
        weatherRepository.insertFav(favoriteModel)
    }

    fun deletebyLatLon(lat: Double, lon: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.deleteFavoriteByLatLon(lat, lon)
        }
    }
    fun insertLocalWeather(myResponse: MyResponse){
        viewModelScope.launch {
            weatherRepository.insertWeatherResponse(myResponse)
        }
    }

    fun getLastLocalWeather():Flow<MyResponse>{
       return weatherRepository.getLastModel()
    }
}