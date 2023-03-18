package com.example.weatherforcast.model

import android.content.Context
import com.example.skyview.database.FavouriteDao
import com.example.skyview.database.LocalDao
import com.example.skyview.model.MyResponse
import kotlinx.coroutines.flow.Flow

interface WeatherRepositoryInterface : FavouriteDao, LocalDao {
    suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        lang:String
    ) : Flow<MyResponse>

    suspend fun getUserChoise(context: Context) : String
}