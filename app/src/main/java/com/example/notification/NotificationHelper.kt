package com.example.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.MainActivity

object NotificationHelper {
    const val CHANNEL_ID = "devotional_reminders_channel"
    const val LOVE_NOTES_CHANNEL_ID = "daily_love_notes_channel"
    const val DEVOTIONAL_NOTIFICATION_ID = 1001
    const val LOVE_NOTE_NOTIFICATION_ID = 1002

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Daily Love Notes Channel
            val loveChannel = NotificationChannel(
                LOVE_NOTES_CHANNEL_ID,
                "Daily Love Notes",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Daily intimate whispers of love and personal encouragement"
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(loveChannel)

            // General Devotional Reminders Channel
            val generalChannel = NotificationChannel(
                CHANNEL_ID,
                "In Christ, My woman Daily Whispers",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Daily personal reminders, messages of love, and guided prayers"
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(generalChannel)
        }
    }

    fun showDailyLoveNoteNotification(context: Context, loveNoteMessage: String) {
        createNotificationChannel(context)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("EXTRA_NAV_TAB", "TODAY")
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            LOVE_NOTE_NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, LOVE_NOTES_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_email)
            .setContentTitle("A Love Note For You ❤️")
            .setContentText(loveNoteMessage)
            .setStyle(NotificationCompat.BigTextStyle().bigText(loveNoteMessage))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        try {
            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(LOVE_NOTE_NOTIFICATION_ID, builder.build())
        } catch (_: SecurityException) {
            // Handled when POST_NOTIFICATIONS is not granted
        }
    }

    fun showDevotionalNotification(context: Context, title: String, content: String) {
        createNotificationChannel(context)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            DEVOTIONAL_NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_email)
            .setContentTitle(title)
            .setContentText(content)
            .setStyle(NotificationCompat.BigTextStyle().bigText(content))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        try {
            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(DEVOTIONAL_NOTIFICATION_ID, builder.build())
        } catch (_: SecurityException) {
            // Handled when permission is not granted
        }
    }
}
