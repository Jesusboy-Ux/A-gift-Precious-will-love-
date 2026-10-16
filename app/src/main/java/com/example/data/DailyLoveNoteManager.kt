package com.example.data

import android.content.Context
import java.util.Calendar

data class LoveNote(
    val id: Int,
    val template: String,
    val category: String = "Love & Encouragement"
)

object DailyLoveNoteManager {
    private const val PREFS_NAME = "daily_love_notes_prefs"
    private const val KEY_USER_NAME = "key_user_preferred_name"
    private const val DEFAULT_NAME = "Beloved"

    val curatedLoveNotes = listOf(
        LoveNote(
            id = 1,
            template = "Hey {name}. Drop your shoulders and take a slow breath. You don't have to carry the whole world today—you are deeply, unconditionally loved."
        ),
        LoveNote(
            id = 2,
            template = "{name}, you are not an afterthought. Every quiet battle you fought in silence was seen, and grace is holding you right now."
        ),
        LoveNote(
            id = 3,
            template = "Sweet {name}, stop striving for a second. You don't have to earn your place today. You are enough right now, exactly as you are."
        ),
        LoveNote(
            id = 4,
            template = "Good morning, {name}. When the noise of the day gets loud, retreat into your inner sanctuary. Stillness and peace are your birthright."
        ),
        LoveNote(
            id = 5,
            template = "{name}, your mistakes do not define you. Mercy was waiting for you at sunrise, fresh and clean. Wipe the slate and walk in freedom."
        ),
        LoveNote(
            id = 6,
            template = "My dear {name}, you have a tender heart, and the world is lucky to have you. Protect your peace today and be gentle with yourself."
        ),
        LoveNote(
            id = 7,
            template = "{name}, feeling anxious? Put your hand on your chest. Feel that steady beat? Love is alive inside you, and this hour is covered."
        ),
        LoveNote(
            id = 8,
            template = "Beloved {name}, you don't have to figure out the next five years today. Just take one faithful, quiet step. Love will light the rest."
        ),
        LoveNote(
            id = 9,
            template = "{name}, when loneliness whispers, remember: you are engraved on the palms of eternal grace. You are never, ever truly alone."
        ),
        LoveNote(
            id = 10,
            template = "Hey {name}. I'm so proud of how resilient you are. Rest tonight knowing that you are deeply treasured beyond measure."
        ),
        LoveNote(
            id = 11,
            template = "{name}, release the need to please everyone today. Your worth was settled long ago by divine love. Walk in confident peace."
        ),
        LoveNote(
            id = 12,
            template = "Sweet soul {name}, let joy surprise you today in the smallest, simplest things—a warm cup, a gentle breeze, a quiet moment."
        ),
        LoveNote(
            id = 13,
            template = "{name}, you are stronger than the worries trying to hold you back. Breathe out panic, breathe in peace. You've got this."
        ),
        LoveNote(
            id = 14,
            template = "{name}, your prayers haven't vanished into the void. They are heard, held, and answered in the fullness of perfect time."
        )
    )

    fun getUserName(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_USER_NAME, DEFAULT_NAME) ?: DEFAULT_NAME
    }

    fun setUserName(context: Context, name: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val cleanName = if (name.trim().isBlank()) DEFAULT_NAME else name.trim()
        prefs.edit().putString(KEY_USER_NAME, cleanName).apply()
    }

    fun getTodayLoveNote(context: Context): String {
        val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val note = curatedLoveNotes[(dayOfYear - 1) % curatedLoveNotes.size]
        val name = getUserName(context)
        return note.template.replace("{name}", name)
    }

    fun getLoveNoteForDay(context: Context, dayIndex: Int): String {
        val note = curatedLoveNotes[Math.abs(dayIndex) % curatedLoveNotes.size]
        val name = getUserName(context)
        return note.template.replace("{name}", name)
    }
}
