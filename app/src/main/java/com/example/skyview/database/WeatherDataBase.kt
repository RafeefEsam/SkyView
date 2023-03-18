package com.example.skyview.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.skyview.model.AlarmPojo
import com.example.skyview.model.FavoriteModel
import com.example.skyview.model.MyResponse


@Database(entities = [FavoriteModel::class, AlarmPojo::class, MyResponse::class], version = 7)
abstract class WeatherDataBase: RoomDatabase() {
    abstract fun weatherDao(): FavouriteDao
    abstract fun alarmDAO(): AlarmDAO

    abstract fun localDao():LocalDao
    companion object {
        @Volatile
        private var INSTANCE: WeatherDataBase? = null

        fun getDatabase(context: Context): WeatherDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context = context.applicationContext,
                    WeatherDataBase::class.java,
                    "app_database"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

