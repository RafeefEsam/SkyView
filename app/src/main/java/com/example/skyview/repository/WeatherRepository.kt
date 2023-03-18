package com.example.weatherforcast.model

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.skyview.database.FavouriteDao
import com.example.skyview.database.LocalDao
import com.example.skyview.model.FavoriteModel
import com.example.skyview.model.MyResponse
import com.example.weatherapp.Networking.APIinterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext

class WeatherRepository(
    var favouriteDao:FavouriteDao,
    val apiClient :APIinterface,
    var localDao: LocalDao
) : WeatherRepositoryInterface {


    override suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        lang:String
    ): Flow<MyResponse> = flow{
        emit(apiClient.getCurrentWeather(lat, lon, lang = lang))
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getUserChoise(context: Context): String =
        withContext(Dispatchers.Main) {
            return@withContext suspendCancellableCoroutine<String> { name ->
                val options = arrayOf("Location", "Map")
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Choose Location or Map")
                    .setItems(options) { dialog, which ->
                        when (which) {
                            0 -> {
                                name.resume("Location", null)
                            }
                            1 -> {
                                name.resume("Map", null)
                            }
                        }
                    }
                    .setNegativeButton("Cancel", null)

                val dialog = builder.create()
                dialog.show()
            }
        }

    override suspend fun insertFav(weatherData: FavoriteModel) {
        favouriteDao.insertFav(weatherData)
    }

    override fun getAllFavWeather(): Flow<List<FavoriteModel>> {
        return favouriteDao.getAllFavWeather()
    }

    override fun deleteWeather(weatherData: FavoriteModel) {
        favouriteDao.deleteWeather(weatherData)
    }

    override fun deleteFavoriteByLatLon(latValue: Double, lonValue: Double){
        favouriteDao.deleteFavoriteByLatLon(latValue, lonValue)
    }

    override suspend fun insertWeatherResponse(weatherResponse: MyResponse) {
        localDao.insertWeatherResponse(weatherResponse)
    }

    override fun getLastModel(): Flow<MyResponse> {
       return localDao.getLastModel()
    }


}