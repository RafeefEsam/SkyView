package com.example.skyview.repository

import com.example.skyview.database.FavouriteDao
import com.example.skyview.database.LocalDao
import com.example.skyview.model.Current
import com.example.skyview.model.FavoriteModel
import com.example.skyview.model.MyResponse
import com.example.weatherapp.Networking.APIinterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeFavouriteDao : FavouriteDao {
    private val weatherData = mutableListOf<FavoriteModel>()

    override suspend fun insertFav(weatherData: FavoriteModel) {
        this.weatherData.add(weatherData)
    }

    override fun getAllFavWeather(): Flow<List<FavoriteModel>> {
        return flow {
            emit(weatherData)
        }
    }

    override fun deleteWeather(weatherData: FavoriteModel) {
        this.weatherData.remove(weatherData)
    }

    override fun deleteFavoriteByLatLon(latValue: Double, lonValue: Double) {
        this.weatherData.removeIf { it.lat == latValue && it.lon == lonValue }
    }
}

class FakeAPIinterface : APIinterface {
    //  private val currentWeatherResponse = MyResponse

    override suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
        exclude: String,
        apiKey: String,
        units: String,
        lang: String
    ): MyResponse {
        return MyResponse(
            0, "", Current(
                0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                0.0, listOf(), 0.0, 0.0
            ),
            listOf(), listOf(), 0.0, 0.0
        )
    }
}

class FakeLocalDao : LocalDao {
    private var myResponse: MyResponse? = null
    override suspend fun insertWeatherResponse(weatherResponse: MyResponse) {
        myResponse = weatherResponse
    }

    override fun getLastModel(): Flow<MyResponse> {
        return flow {
            myResponse?.let { emit(it) }
        }
    }

}

