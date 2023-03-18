package com.example.skyview.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.skyview.model.MyResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWeatherResponse(weatherResponse: MyResponse)

    @Query("SELECT * FROM weatherResponse ORDER BY id DESC LIMIT 1")
    fun getLastModel(): Flow<MyResponse>

}