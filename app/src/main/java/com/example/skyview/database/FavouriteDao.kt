package com.example.skyview.database

import androidx.room.*
import com.example.skyview.model.FavoriteModel
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFav(weatherData: FavoriteModel)

    @Query("SELECT * FROM favorite")
    fun getAllFavWeather(): Flow<List<FavoriteModel>>

    @Delete
    fun deleteWeather(weatherData: FavoriteModel)

    @Query("DELETE FROM favorite WHERE lat = :latValue AND lon = :lonValue")
    fun deleteFavoriteByLatLon(latValue: Double, lonValue: Double)

}