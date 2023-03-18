package com.example.skyview.viewmodel.viewmodel

import android.content.Context
import com.example.skyview.model.FavoriteModel
import com.example.skyview.model.MyResponse
import com.example.weatherforcast.model.WeatherRepositoryInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow

class FakeWeatherRepository : WeatherRepositoryInterface {

    private val currentWeatherFlow = MutableStateFlow<MyResponse?>(null)
    private val shouldReturnErrorFlow = MutableStateFlow(false)
    private val favorites = mutableListOf<FavoriteModel>()
    private var myResponse:MyResponse? = null
//
//    fun setCurrentWeather(weatherData: LocationData) {
//        currentWeatherFlow.value = MyResponse
//    }

    fun setShouldReturnError(value: Boolean) {
        shouldReturnErrorFlow.value = value
    }

    override suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        lang: String
    ): Flow<MyResponse> {
        if (shouldReturnErrorFlow.value) {
            return flow {
                currentWeatherFlow.value?.let { emit(it) }
            }
        } else {
            return currentWeatherFlow.filterNotNull()
        }
    }

    override suspend fun getUserChoise(context: Context): String {
        // Do nothing in the fake repository
        return ""
    }

    override suspend fun insertFav(weatherData: FavoriteModel) {
        favorites.add(weatherData)
    }

    override fun getAllFavWeather(): Flow<List<FavoriteModel>> {
        return flow {
            emit(favorites)
        }
    }

    override fun deleteWeather(weatherData: FavoriteModel) {
        favorites.remove(weatherData)
    }

    override fun deleteFavoriteByLatLon(latValue: Double, lonValue: Double){
        favorites.removeAll { it.lat == latValue && it.lon == lonValue }
    }

    override suspend fun insertWeatherResponse(weatherResponse: MyResponse) {
        myResponse = weatherResponse
    }

    override fun getLastModel(): Flow<MyResponse> {
        return flow {
            myResponse?.let { emit(it) }
        }
    }
}