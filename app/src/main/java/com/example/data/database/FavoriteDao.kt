package com.example.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorites ORDER BY timestamp DESC")
    fun getAllFavorites(): Flow<List<FavoriteEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE id = :id)")
    fun isFavorite(id: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE id = :id")
    suspend fun deleteFavoriteById(id: String)

    // Fan wishes
    @Query("SELECT * FROM fan_wishes ORDER BY timestamp DESC")
    fun getAllFanWishes(): Flow<List<FanWishEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFanWish(wish: FanWishEntity)

    @Query("DELETE FROM fan_wishes WHERE id = :id")
    suspend fun deleteFanWish(id: Int)
}
