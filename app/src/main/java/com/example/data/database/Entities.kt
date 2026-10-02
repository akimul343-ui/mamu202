package com.example.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val id: String,
    val type: String, // "MOVIE", "SONG", "DIALOGUE", "SOCIAL"
    val title: String,
    val subtitle: String,
    val actionUrl: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "fan_wishes")
data class FanWishEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val author: String,
    val message: String,
    val location: String,
    val timestamp: Long = System.currentTimeMillis()
)
