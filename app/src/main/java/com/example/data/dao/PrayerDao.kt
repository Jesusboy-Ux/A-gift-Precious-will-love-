package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.PrayerJournalEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface PrayerDao {
    @Query("SELECT * FROM prayer_entries ORDER BY createdAt DESC")
    fun getAllPrayers(): Flow<List<PrayerJournalEntry>>

    @Query("SELECT * FROM prayer_entries WHERE isAnswered = 0 ORDER BY createdAt DESC")
    fun getActivePrayers(): Flow<List<PrayerJournalEntry>>

    @Query("SELECT * FROM prayer_entries WHERE isAnswered = 1 ORDER BY answeredAt DESC")
    fun getAnsweredPrayers(): Flow<List<PrayerJournalEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrayer(entry: PrayerJournalEntry): Long

    @Update
    suspend fun updatePrayer(entry: PrayerJournalEntry)

    @Query("DELETE FROM prayer_entries WHERE id = :id")
    suspend fun deletePrayerById(id: Long)

    @Query("UPDATE prayer_entries SET isAnswered = :answered, answeredAt = :answeredAt, answeredNote = :note WHERE id = :id")
    suspend fun markAnswered(id: Long, answered: Boolean, answeredAt: Long?, note: String?)
}
