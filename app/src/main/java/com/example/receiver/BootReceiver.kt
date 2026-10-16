package com.example.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.notification.ReminderScheduler

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            if (ReminderScheduler.isReminderEnabled(context)) {
                val (hour, minute) = ReminderScheduler.getReminderTime(context)
                ReminderScheduler.scheduleDailyReminder(context, hour, minute)
            }
        }
    }
}
