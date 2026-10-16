package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.notification.NotificationHelper
import com.example.notification.ReminderScheduler
import com.example.ui.components.BreathingPacerModal
import com.example.ui.screens.HeartJournalScreen
import com.example.ui.screens.MessagesVaultScreen
import com.example.ui.screens.StillnessStatsScreen
import com.example.ui.screens.TodayScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.RoseGold
import com.example.ui.theme.SacredGold
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.DevotionalViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize local notification channels and verify schedule
        NotificationHelper.createNotificationChannel(this)
        if (ReminderScheduler.isReminderEnabled(this)) {
            val (hour, min) = ReminderScheduler.getReminderTime(this)
            ReminderScheduler.scheduleDailyReminder(this, hour, min)
        }

        setContent {
            MyApplicationTheme {
                SanctuaryApp()
            }
        }
    }
}

@Composable
fun SanctuaryApp(
    viewModel: DevotionalViewModel = viewModel()
) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val todayDevotional by viewModel.todayDevotional.collectAsStateWithLifecycle()
    val allDevotionals by viewModel.allDevotionals.collectAsStateWithLifecycle()
    val allPrayers by viewModel.allPrayers.collectAsStateWithLifecycle()
    val streakRecord by viewModel.streakRecord.collectAsStateWithLifecycle()
    val isSpeaking by viewModel.isSpeaking.collectAsStateWithLifecycle()
    val userName by viewModel.userName.collectAsStateWithLifecycle()
    val todayLoveNote by viewModel.todayLoveNote.collectAsStateWithLifecycle()

    var showBreathing by remember { mutableStateOf(false) }

    // Custom back handler to return to Today tab if navigated to another tab
    if (currentTab != AppTab.TODAY) {
        BackHandler {
            viewModel.currentTab.value = AppTab.TODAY
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("sanctuary_main_scaffold"),
        contentWindowInsets = WindowInsets.navigationBars,
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp,
                modifier = Modifier.testTag("sanctuary_bottom_nav")
            ) {
                // Tab 1: Today
                NavigationBarItem(
                    selected = currentTab == AppTab.TODAY,
                    onClick = { viewModel.currentTab.value = AppTab.TODAY },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == AppTab.TODAY) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Today",
                            tint = if (currentTab == AppTab.TODAY) RoseGold else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    label = { Text("Today", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.testTag("nav_item_today")
                )

                // Tab 2: Love Notes / Messages Vault
                NavigationBarItem(
                    selected = currentTab == AppTab.MESSAGES,
                    onClick = { viewModel.currentTab.value = AppTab.MESSAGES },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == AppTab.MESSAGES) Icons.Filled.Mail else Icons.Outlined.Mail,
                            contentDescription = "Love Notes",
                            tint = if (currentTab == AppTab.MESSAGES) RoseGold else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    label = { Text("Love Notes", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.testTag("nav_item_messages")
                )

                // Tab 3: Heart Journal
                NavigationBarItem(
                    selected = currentTab == AppTab.JOURNAL,
                    onClick = { viewModel.currentTab.value = AppTab.JOURNAL },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == AppTab.JOURNAL) Icons.Filled.VolunteerActivism else Icons.Outlined.EditNote,
                            contentDescription = "Heart Journal",
                            tint = if (currentTab == AppTab.JOURNAL) SacredGold else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    label = { Text("Journal", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.testTag("nav_item_journal")
                )

                // Tab 4: Stillness & Routine
                NavigationBarItem(
                    selected = currentTab == AppTab.STREAK,
                    onClick = { viewModel.currentTab.value = AppTab.STREAK },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == AppTab.STREAK) Icons.Filled.Spa else Icons.Outlined.Spa,
                            contentDescription = "Routine",
                            tint = if (currentTab == AppTab.STREAK) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    label = { Text("Routine", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.testTag("nav_item_streak")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                AppTab.TODAY -> {
                    TodayScreen(
                        devotional = todayDevotional,
                        loveNote = todayLoveNote,
                        userName = userName,
                        onUpdateUserName = { newName ->
                            viewModel.updateUserName(newName)
                        },
                        onSpeakLoveNote = {
                            viewModel.speakLoveNote()
                        },
                        onSendTestLoveNoteNotification = {
                            viewModel.sendInstantLoveNoteNotification()
                        },
                        isSpeaking = isSpeaking,
                        onSpeakClick = {
                            todayDevotional?.let { viewModel.speakDevotional(it) }
                        },
                        onStopSpeakClick = {
                            viewModel.stopSpeaking()
                        },
                        onOpenBreathing = {
                            showBreathing = true
                        },
                        onToggleFavorite = { id, currentFav ->
                            viewModel.toggleFavorite(id, currentFav)
                        }
                    )
                }

                AppTab.MESSAGES -> {
                    MessagesVaultScreen(
                        devotionals = allDevotionals,
                        isSpeaking = isSpeaking,
                        onSpeakMessage = { msg ->
                            viewModel.speakDevotional(msg)
                        },
                        onStopSpeak = {
                            viewModel.stopSpeaking()
                        },
                        onToggleFavorite = { id, currentFav ->
                            viewModel.toggleFavorite(id, currentFav)
                        }
                    )
                }

                AppTab.JOURNAL -> {
                    HeartJournalScreen(
                        prayers = allPrayers,
                        onAddPrayer = { title, body, cat ->
                            viewModel.addPrayer(title, body, cat)
                        },
                        onDeletePrayer = { id ->
                            viewModel.deletePrayer(id)
                        },
                        onMarkAnswered = { id, answered, note ->
                            viewModel.markPrayerAnswered(id, answered, note)
                        }
                    )
                }

                AppTab.STREAK -> {
                    StillnessStatsScreen(
                        streak = streakRecord,
                        userName = userName,
                        onOpenBreathing = {
                            showBreathing = true
                        }
                    )
                }
            }
        }

        // Breathing Modal
        if (showBreathing) {
            BreathingPacerModal(onDismiss = { showBreathing = false })
        }
    }
}
