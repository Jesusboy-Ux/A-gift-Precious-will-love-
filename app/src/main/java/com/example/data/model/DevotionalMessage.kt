package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "devotionals")
data class DevotionalMessage(
    @PrimaryKey val id: Int,
    val dayIndex: Int,
    val title: String,
    val personalLetter: String,
    val scriptureVerse: String,
    val scriptureRef: String,
    val reflection: String,
    val guidedPrayer: String,
    val mindfulPrompt: String,
    val category: String,
    val isFavorite: Boolean = false
)
