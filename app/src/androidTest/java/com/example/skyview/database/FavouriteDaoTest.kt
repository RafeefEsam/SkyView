package com.example.skyview.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.example.skyview.model.FavoriteModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class FavouriteDaoTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: WeatherDataBase
    private lateinit var favouriteDao: FavouriteDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            WeatherDataBase::class.java
        )
            .allowMainThreadQueries()
            .build()
        favouriteDao = database.weatherDao()
    }

    @After
    fun closeDB() {
        database.close()
    }

    @Test
    fun insertAndGetAllWeatherData() = runBlockingTest {
        // Given a new weather data
        val weatherData = FavoriteModel(51.5074, -0.1278)

        // When inserting the weather data
        favouriteDao.insertFav(weatherData)

        // Then the inserted weather data can be retrieved
        val allWeatherData = favouriteDao.getAllFavWeather().first()
        assertThat(allWeatherData.size, `is`(1))
        assertThat(allWeatherData[0], `is`(weatherData))
    }

    @Test
    fun deleteAndGetAllWeatherData() = runBlockingTest {
        // Given a weather data to delete
        val weatherData = FavoriteModel(51.5074, -0.1278)
        favouriteDao.insertFav(weatherData)

        // When deleting the weather data
        favouriteDao.deleteWeather(weatherData)

        // Then the deleted weather data should not be returned
        val allWeatherData = favouriteDao.getAllFavWeather().first()
        assertThat(allWeatherData.size, `is`(0))
    }

    @Test
    fun deleteFavoriteByLatLon() = runBlockingTest {
        // Given a weather data with a specific lat and lon
        val lat = 51.5074
        val lon = -0.1278
        val weatherData = FavoriteModel(lat, lon)
        favouriteDao.insertFav(weatherData)

        // When deleting the weather data by lat and lon
        favouriteDao.deleteFavoriteByLatLon(lat, lon)

        // Then the deleted weather data should not be returned
        val allWeatherData = favouriteDao.getAllFavWeather().first()
        assertThat(allWeatherData.size, `is`(0))
    }
}