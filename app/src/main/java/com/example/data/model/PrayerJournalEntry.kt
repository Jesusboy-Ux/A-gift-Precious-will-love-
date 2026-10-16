package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prayer_entries")
data class PrayerJournalEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val body: String,
    val category: String, // e.g. "Gratitude", "Healing", "Guidance", "Peace", "Love"
    val createdAt: Long = System.currentTimeMillis(),
    val isAnswered: Boolean = false,
    val answeredAt: Long? = null,
    val answeredNote: String? = null
)
