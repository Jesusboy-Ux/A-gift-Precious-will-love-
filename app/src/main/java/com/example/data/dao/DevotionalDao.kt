package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.DevotionalMessage
import kotlinx.coroutines.flow.Flow

@Dao
interface DevotionalDao {
    @Query("SELECT * FROM devotionals ORDER BY dayIndex ASC")
    fun getAllDevotionals(): Flow<List<DevotionalMessage>>

    @Query("SELECT * FROM devotionals WHERE isFavorite = 1 ORDER BY dayIndex ASC")
    fun getFavorites(): Flow<List<DevotionalMessage>>

    @Query("SELECT * FROM devotionals WHERE id = :id LIMIT 1")
    fun getDevotionalById(id: Int): Flow<DevotionalMessage?>

    @Query("SELECT * FROM devotionals WHERE category = :category ORDER BY dayIndex ASC")
    fun getDevotionalsByCategory(category: String): Flow<List<DevotionalMessage>>

    @Query("UPDATE devotionals SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: Int, isFavorite: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(devotionals: List<DevotionalMessage>)

    @Query("SELECT COUNT(*) FROM devotionals")
    suspend fun getCount(): Int
}
