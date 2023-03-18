package com.example.skyview.viewmodel.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.skyview.model.ApiState
import com.example.skyview.model.FavoriteModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class WeatherViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: WeatherViewModel
    private lateinit var fakeRepository: FakeWeatherRepository

    @Before
    fun setUp() {
        fakeRepository = FakeWeatherRepository()
        viewModel = WeatherViewModel(fakeRepository)
    }

    @Test
    fun getCurrentWeatherFromAPI_Loading() = runBlocking {

        viewModel.getCurrentWeatherFromAPI(0.0, 0.0,"ar")

        val data = viewModel.data.first()

       assertTrue(data is ApiState.Loading)

    }

    @Test
    fun insertToFav() = runBlockingTest {
        val favoriteModel = FavoriteModel(lat = 0.0, lon = 0.0)

        viewModel.insetToFav(favoriteModel)

        val favorites = viewModel.favWeather.value
        assertTrue(favorites?.contains(favoriteModel)?:true)
    }

    @Test
    fun deletebyLatLon() = runBlocking {
        val favoriteModel = FavoriteModel(lat = 0.0, lon = 0.0)
        fakeRepository.insertFav(favoriteModel)

        viewModel.deletebyLatLon(0.0, 0.0)

        val favorites = viewModel.favWeather.value
        assertFalse(favorites?.contains(favoriteModel)?:false)
    }
}

