package com.example.util

import android.content.Context
import android.content.Intent
import com.example.data.model.DevotionalMessage

object ShareHelper {

    fun shareLoveNote(context: Context, loveNote: String, userName: String) {
        val shareText = buildString {
            append("💌 A Daily Love Note for you:\n\n")
            append("“$loveNote”\n\n")
            append("— In Christ, My woman ❤️")
        }

        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "A Daily Love Note For You")
            putExtra(Intent.EXTRA_TEXT, shareText)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        val chooserIntent = Intent.createChooser(sendIntent, "Share Daily Love Note via...")
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooserIntent)
    }

    fun shareScripture(context: Context, verse: String, reference: String) {
        val shareText = buildString {
            append("📖 Scripture of the Day:\n\n")
            append("“$verse”\n")
            append("— $reference\n\n")
            append("In Christ, My woman 🕊️")
        }

        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Daily Scripture: $reference")
            putExtra(Intent.EXTRA_TEXT, shareText)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        val chooserIntent = Intent.createChooser(sendIntent, "Share Scripture via...")
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooserIntent)
    }

    fun shareFullDevotional(context: Context, devotional: DevotionalMessage, loveNote: String) {
        val shareText = buildString {
            append("🕊️ In Christ, My woman: ${devotional.title}\n\n")
            append("💌 Daily Love Note:\n")
            append("“$loveNote”\n\n")
            append("📖 Anchor Scripture:\n")
            append("“${devotional.scriptureVerse}” — ${devotional.scriptureRef}\n\n")
            append("🌿 Heart-to-Heart Prayer:\n")
            append("${devotional.guidedPrayer}\n\n")
            append("May peace and unconditional love surround your day.")
        }

        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "In Christ, My woman: ${devotional.title}")
            putExtra(Intent.EXTRA_TEXT, shareText)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        val chooserIntent = Intent.createChooser(sendIntent, "Share Devotional via...")
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooserIntent)
    }
}
