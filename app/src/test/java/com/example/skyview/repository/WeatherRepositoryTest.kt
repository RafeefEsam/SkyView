package com.example.skyview.repository

import com.example.skyview.database.FavouriteDao
import com.example.skyview.database.LocalDao
import com.example.skyview.model.FavoriteModel
import com.example.weatherapp.Networking.APIinterface
import com.example.weatherforcast.model.WeatherRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.core.Is.`is`
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class WeatherRepositoryTest {
    private lateinit var fakeDao: FavouriteDao
    private lateinit var fakeApi: APIinterface
    private lateinit var fackLocalDao: LocalDao
    private lateinit var repository: WeatherRepository

    @Before
    fun setup() {
        fakeApi = FakeAPIinterface()
        fakeDao = FakeFavouriteDao()
        fackLocalDao = FakeLocalDao()
        repository = WeatherRepository( fakeDao, fakeApi, fackLocalDao)
    }

    @Test
    fun testGetCurrentWeather() = runBlocking {
        val response = repository.getCurrentWeather(0.0, 0.0, "en").first()
       // assertTrue(response is MyResponse)
        assertThat(response.lat, `is`(0.0))
    }

    @Test
    fun testInsertAndGetAllWeatherData() = runBlocking {
        val weatherData = FavoriteModel(0.0, 0.0)
        repository.insertFav(weatherData)
        val allWeatherData = repository.getAllFavWeather().first()
        assertEquals(allWeatherData.size, 1)
        assertEquals(allWeatherData[0], weatherData)
    }

    @Test
    fun testDeleteWeather() = runBlocking {
        val weatherData = FavoriteModel(0.0, 0.0)
        repository.insertFav(weatherData)
        repository.deleteWeather(weatherData)
        val allWeatherData = repository.getAllFavWeather().first()
        assertEquals(allWeatherData.size, 0)
    }

    @Test
    fun testDeleteFavoriteByLatLon() = runBlocking {
        val weatherData1 = FavoriteModel(0.0, 0.0)
        val weatherData2 = FavoriteModel(1.0, 1.0)
        repository.insertFav(weatherData1)
        repository.insertFav(weatherData2)
        repository.deleteFavoriteByLatLon(0.0, 0.0)
        val allWeatherData = repository.getAllFavWeather().first()
        assertEquals(allWeatherData.size, 1)
        assertEquals(allWeatherData[0], weatherData2)
    }
}
