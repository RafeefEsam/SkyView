package com.example.weatherapp.Networking

import com.example.skyview.model.MyResponse
import com.example.skyview.utils.Constants
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Query

interface APIinterface {
    @GET("onecall")
    suspend fun getCurrentWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("exclude") exclude: String = Constants.EXCLUDE,
        @Query("appid") apiKey: String = Constants.API_KEY,
        @Query("units") units: String = Constants.CELSIUS,
        @Query("lang") lang: String = Constants.ENGLISH
    ) : MyResponse



}