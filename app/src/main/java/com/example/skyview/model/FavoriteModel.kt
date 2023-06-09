package com.example.skyview.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "favorite")
data class FavoriteModel(
    @PrimaryKey
    @ColumnInfo(name = "lat")
    val lat:Double,
    @ColumnInfo(name = "lon")
    val lon: Double
): Serializable