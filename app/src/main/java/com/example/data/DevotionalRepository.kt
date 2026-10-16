package com.example.data

import com.example.data.dao.DevotionalDao
import com.example.data.dao.PrayerDao
import com.example.data.dao.StreakDao
import com.example.data.model.DevotionalMessage
import com.example.data.model.PrayerJournalEntry
import com.example.data.model.StreakRecord
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DevotionalRepository(
    private val devotionalDao: DevotionalDao,
    private val prayerDao: PrayerDao,
    private val streakDao: StreakDao
) {
    init {
        CoroutineScope(Dispatchers.IO).launch {
            seedInitialDataIfNeeded()
        }
    }

    private suspend fun seedInitialDataIfNeeded() {
        if (devotionalDao.getCount() == 0) {
            devotionalDao.insertAll(DevotionalSeedData.devotionals)
        }
        val streak = streakDao.getStreak().firstOrNull()
        if (streak == null) {
            streakDao.insertOrUpdate(
                StreakRecord(
                    id = 1,
                    currentStreak = 1,
                    longestStreak = 1,
                    lastReadDate = todayDateString(),
                    totalDaysRead = 1
                )
            )
        }
        // Seed an initial heartfelt sample prayer to inspire the user
        val existingPrayers = prayerDao.getAllPrayers().firstOrNull()
        if (existingPrayers.isNullOrEmpty()) {
            prayerDao.insertPrayer(
                PrayerJournalEntry(
                    title = "Inner Peace and Guarding My Heart",
                    body = "Lord, grant me stillness in the middle of a noisy schedule. Guard my thoughts from anxiety and help me walk in kindness today.",
                    category = "Peace",
                    createdAt = System.currentTimeMillis() - 86400000L * 2,
                    isAnswered = true,
                    answeredAt = System.currentTimeMillis() - 86400000L,
                    answeredNote = "Felt deep reassurance during morning prayer and handled a stressful meeting with total calm."
                )
            )
            prayerDao.insertPrayer(
                PrayerJournalEntry(
                    title = "Clarity for New Opportunities",
                    body = "Seeking divine wisdom and discernment on which steps to take next. Help me trust the process and rest in Your timing.",
                    category = "Guidance",
                    createdAt = System.currentTimeMillis() - 3600000L * 5,
                    isAnswered = false
                )
            )
        }
    }

    fun getAllDevotionals(): Flow<List<DevotionalMessage>> = devotionalDao.getAllDevotionals()

    fun getFavorites(): Flow<List<DevotionalMessage>> = devotionalDao.getFavorites()

    fun getDevotionalsByCategory(category: String): Flow<List<DevotionalMessage>> =
        devotionalDao.getDevotionalsByCategory(category)

    fun getDevotionalById(id: Int): Flow<DevotionalMessage?> = devotionalDao.getDevotionalById(id)

    suspend fun toggleFavorite(id: Int, isFavorite: Boolean) {
        devotionalDao.updateFavorite(id, isFavorite)
    }

    // Prayers
    fun getAllPrayers(): Flow<List<PrayerJournalEntry>> = prayerDao.getAllPrayers()

    fun getActivePrayers(): Flow<List<PrayerJournalEntry>> = prayerDao.getActivePrayers()

    fun getAnsweredPrayers(): Flow<List<PrayerJournalEntry>> = prayerDao.getAnsweredPrayers()

    suspend fun addPrayer(title: String, body: String, category: String): Long {
        return prayerDao.insertPrayer(
            PrayerJournalEntry(
                title = title.trim(),
                body = body.trim(),
                category = category
            )
        )
    }

    suspend fun updatePrayer(entry: PrayerJournalEntry) {
        prayerDao.updatePrayer(entry)
    }

    suspend fun deletePrayer(id: Long) {
        prayerDao.deletePrayerById(id)
    }

    suspend fun markAnswered(id: Long, answered: Boolean, note: String?) {
        val answeredAt = if (answered) System.currentTimeMillis() else null
        prayerDao.markAnswered(id, answered, answeredAt, note)
    }

    // Streak
    fun getStreak(): Flow<StreakRecord?> = streakDao.getStreak()

    suspend fun recordReadingStreak() {
        val today = todayDateString()
        val currentRecord = streakDao.getStreak().firstOrNull() ?: StreakRecord(id = 1)
        if (currentRecord.lastReadDate == today) {
            return // already recorded today
        }

        val yesterday = yesterdayDateString()
        val isConsecutive = currentRecord.lastReadDate == yesterday
        val newCurrent = if (isConsecutive) currentRecord.currentStreak + 1 else 1
        val newLongest = maxOf(currentRecord.longestStreak, newCurrent)
        val newTotal = currentRecord.totalDaysRead + 1

        streakDao.insertOrUpdate(
            currentRecord.copy(
                currentStreak = newCurrent,
                longestStreak = newLongest,
                lastReadDate = today,
                totalDaysRead = newTotal
            )
        )
    }

    private fun todayDateString(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    private fun yesterdayDateString(): String {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -1)
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)
    }
}
