package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.DailyLoveNoteManager
import com.example.data.DevotionalRepository
import com.example.data.model.DevotionalMessage
import com.example.data.model.PrayerJournalEntry
import com.example.data.model.StreakRecord
import com.example.notification.NotificationHelper
import com.example.util.SpeechHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

enum class AppTab(val title: String) {
    TODAY("Today"),
    MESSAGES("Love Notes"),
    JOURNAL("Heart Journal"),
    STREAK("Stillness & Stats")
}

class DevotionalViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = DevotionalRepository(
        db.devotionalDao(),
        db.prayerDao(),
        db.streakDao()
    )
    val speechHelper = SpeechHelper(application)

    val currentTab = MutableStateFlow(AppTab.TODAY)

    val userName = MutableStateFlow(DailyLoveNoteManager.getUserName(application))

    val todayLoveNote: StateFlow<String> = userName.map {
        DailyLoveNoteManager.getTodayLoveNote(application)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        DailyLoveNoteManager.getTodayLoveNote(application)
    )

    val allDevotionals: StateFlow<List<DevotionalMessage>> = repository.getAllDevotionals()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val todayDevotional: StateFlow<DevotionalMessage?> = allDevotionals.map { list ->
        if (list.isEmpty()) null
        else {
            val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
            val index = (dayOfYear - 1) % list.size
            list[index]
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allPrayers: StateFlow<List<PrayerJournalEntry>> = repository.getAllPrayers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val streakRecord: StateFlow<StreakRecord?> = repository.getStreak()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val selectedDetailMessage = MutableStateFlow<DevotionalMessage?>(null)

    val showBreathingModal = MutableStateFlow(false)

    val isSpeaking: StateFlow<Boolean> = speechHelper.isSpeaking

    init {
        // Record reading streak on launch
        viewModelScope.launch {
            repository.recordReadingStreak()
        }
    }

    fun updateUserName(name: String) {
        DailyLoveNoteManager.setUserName(getApplication(), name)
        userName.value = DailyLoveNoteManager.getUserName(getApplication())
    }

    fun sendInstantLoveNoteNotification() {
        val note = DailyLoveNoteManager.getTodayLoveNote(getApplication())
        NotificationHelper.showDailyLoveNoteNotification(getApplication(), note)
    }

    fun speakLoveNote() {
        val note = todayLoveNote.value
        speechHelper.speak("Here is your love note: $note")
    }

    fun toggleFavorite(id: Int, currentFav: Boolean) {
        viewModelScope.launch {
            repository.toggleFavorite(id, !currentFav)
        }
    }

    fun speakDevotional(message: DevotionalMessage) {
        val spokenText = buildString {
            append("A message of love for you today: ")
            append(message.title)
            append(". ")
            append(message.personalLetter)
            append(". Scripture: ")
            append(message.scriptureVerse)
            append(". ")
            append(message.scriptureRef)
            append(". Reflection: ")
            append(message.reflection)
            append(". Guided Prayer: ")
            append(message.guidedPrayer)
        }
        speechHelper.speak(spokenText)
    }

    fun speakText(text: String) {
        speechHelper.speak(text)
    }

    fun stopSpeaking() {
        speechHelper.stop()
    }

    fun addPrayer(title: String, body: String, category: String) {
        viewModelScope.launch {
            repository.addPrayer(title, body, category)
        }
    }

    fun updatePrayer(entry: PrayerJournalEntry) {
        viewModelScope.launch {
            repository.updatePrayer(entry)
        }
    }

    fun deletePrayer(id: Long) {
        viewModelScope.launch {
            repository.deletePrayer(id)
        }
    }

    fun markPrayerAnswered(id: Long, answered: Boolean, note: String?) {
        viewModelScope.launch {
            repository.markAnswered(id, answered, note)
        }
    }

    override fun onCleared() {
        super.onCleared()
        speechHelper.shutdown()
    }
}
