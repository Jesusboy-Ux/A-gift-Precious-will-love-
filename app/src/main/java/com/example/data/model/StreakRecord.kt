package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "streak_record")
data class StreakRecord(
    @PrimaryKey val id: Int = 1,
    val currentStreak: Int = 1,
    val longestStreak: Int = 1,
    val lastReadDate: String = "",
    val totalDaysRead: Int = 1
)
