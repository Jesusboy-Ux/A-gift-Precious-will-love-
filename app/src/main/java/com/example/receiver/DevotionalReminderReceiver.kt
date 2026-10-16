package com.example.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.data.DailyLoveNoteManager
import com.example.notification.NotificationHelper
import com.example.notification.ReminderScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DevotionalReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Fetch personalized short daily love note
                val loveNote = DailyLoveNoteManager.getTodayLoveNote(context)
                NotificationHelper.showDailyLoveNoteNotification(context, loveNote)

                // Reschedule for next day
                val (hour, minute) = ReminderScheduler.getReminderTime(context)
                if (ReminderScheduler.isReminderEnabled(context)) {
                    ReminderScheduler.scheduleDailyReminder(context, hour, minute)
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}
