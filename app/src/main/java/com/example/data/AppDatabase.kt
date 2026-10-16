package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.dao.DevotionalDao
import com.example.data.dao.PrayerDao
import com.example.data.dao.StreakDao
import com.example.data.model.DevotionalMessage
import com.example.data.model.PrayerJournalEntry
import com.example.data.model.StreakRecord

@Database(
    entities = [
        DevotionalMessage::class,
        PrayerJournalEntry::class,
        StreakRecord::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun devotionalDao(): DevotionalDao
    abstract fun prayerDao(): PrayerDao
    abstract fun streakDao(): StreakDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sanctuary_devotional_db"
                )
                    .fallbackToDestructiveMigration(false)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
