# Sanctuary: Love Notes & Daily Devotional

An intimate, heartfelt devotional and private sanctuary designed to speak in a deeply personal, natural, and loving tone. It delivers daily personal messages—like private letters and spoken whispers of unconditional love, reassurance, scripture, and mindfulness—combined with a heart-to-heart prayer journal and local Android reminders.

## User Review & Critical Decisions

> [!IMPORTANT]
> **Key Revisions Incorporated from Your Feedback**:
> - **Natural, Intimate & Personal Voice**: The devotionals are not cold or textbook theological treatises; they are written and spoken in a warm, authentic, affectionate voice—like a private, loving letter written directly to the reader's heart ("A Private Note for You", "From My Heart to Yours").
> - **Love as the Central Heartbeat**: Unconditional love, reassurance, comfort, grace, and heartfelt tenderness are woven through every daily reading, scripture, and prayer.
> - **Spoken Audio Messages**: Integrated spoken voice player (using Android's native Text-to-Speech with a gentle, measured cadence) allowing the app to literally "speak" the personal message and love notes aloud.
> - **Custom & Authored Messages**: A dedicated "Love Letters & Whispers" section where private messages can be saved, reread, or custom love notes authored and dated.

- **Confirmed Notification Strategy**: Local Android daily reminders (`AlarmManager` + `NotificationManager`) scheduled directly on your device with no external server or login barriers.
- **Confirmed Data Persistence**: Fast, 100% private on-device Room SQLite database preserving all your personal journal entries, favorite love letters, and answered prayers offline.

---

## 1. Overview & Core Concept

**Sanctuary** bridges the gap between sacred daily scriptures and the warmth of a loving, personal conversation. Each morning and evening, the app provides:
1. **A Personal Message of Love**: An intimate note speaking directly to the user in a natural, tender voice—addressing feelings of exhaustion, longing, self-worth, and hope.
2. **Mindful Scripture**: Passages highlighting God's unending love, peace, presence, and mindfulness.
3. **Spoken Voice**: A single tap to have the intimate devotional spoken softly aloud.
4. **Breath of Stillness**: A 1-minute calming breathing pacer to settle anxious thoughts.
5. **Heart Journal & Answered Prayers**: A private vault to write thoughts, prayers, gratitude, and heartfelt notes.

---

## 2. User Experience & Visual Design

### Key User Flows
1. **Daily Message & Love Note Flow**:
   - Opens to a warm greeting ("Welcome, beloved" / "Take a breath, you are safe here").
   - Displays **Today's Private Message**: written in your natural tone, filled with genuine love, empathy, and spiritual grounding.
   - **Listen to Message**: Plays the personal devotional aloud with gentle voice synthesis.
   - **Mindfulness & Prayer**: Scripture of the day followed by a gentle conversational prayer.
2. **Love Letters & Whispers Vault (Messages Tab)**:
   - A curated archive of personal letters categorized by heart states: *When You Feel Overwhelmed*, *When You Need to Remember You Are Loved*, *Morning Encouragement*, *Rest for the Night*.
   - Option to compose and save your own private letters and love notes.
3. **Heart Journal & Prayer Wall**:
   - Write private prayers, hopes, and journal reflections.
   - Track active prayers and celebrate "Answered Prayers" with a golden badge and gratitude note.
4. **Daily Reminders & Settings**:
   - Set a daily reminder time for your morning or evening love note.
   - Customize font size and dark/light ambient reading themes.

### Visual Identity & Theme
- **Vibe**: *Warm, Intimate, Sacred & Reassuring*.
- **Color Palette (M3)**:
  - *Primary*: Deep Twilight Mauve & Velvet Plum (`#2D1B2E` / `#D48B97`)
  - *Accent*: Warm Radiant Gold (`#D4AF37`) & Soft Rose Quartz (`#F2D7D9`)
  - *Surface & Cards*: Soft Linen Parchment (`#FAF7F5`) with tender rounded containers (`24.dp`)
- **Typography**:
  - Scripture & Love Note Titles: Warm Serif font with generous line spacing (`26.sp`).
  - Body Text: Clean, legible sans-serif with natural conversational cadence.

---

## 3. Technical Architecture & Data Strategy

### System Architecture Diagram

```
┌────────────────────────────────────────────────────────────────────────┐
│                          Jetpack Compose UI                            │
│  ┌──────────────────┐  ┌──────────────────┐  ┌───────────────────────┐  │
│  │   TodayScreen    │  │  MessagesVault   │  │     HeartJournal      │  │
│  │(Love Note & Pray)│  │ (Personal Letters│  │(Prayers & Reflections)│  │
│  └─────────┬────────┘  └─────────┬────────┘  └───────────┬───────────┘  │
│            │                     │                       │              │
│            ▼                     ▼                       ▼              │
│  ┌───────────────────────────────────────────────────────────────────┐  │
│  │                       DevotionalViewModel                         │  │
│  │        StateFlow<TodayState>, StateFlow<List<PersonalMessage>>    │  │
│  │             TextToSpeechManager (Voice Audio reciter)             │  │
│  └──────────────────────────────────┬────────────────────────────────┘  │
└─────────────────────────────────────┼──────────────────────────────────┘
                                      │
            ┌─────────────────────────┴────────────────────────┐
            │                                                  │
            ▼                                                  ▼
┌───────────────────────────────┐              ┌───────────────────────────┐
│     DevotionalRepository      │              │    ReminderScheduler      │
│  - Today's Love Message       │              │  - AlarmManager           │
│  - Personal Letters archive   │              │  - DevotionalReceiver     │
│  - Heart Journal CRUD         │              │  - NotificationManager    │
│  - Answered prayer milestones │              └───────────────────────────┘
└───────────────┬───────────────┘
                │
                ▼
┌───────────────────────────────┐
│         AppDatabase           │
│  ┌─────────────────────────┐  │
│  │ LoveMessageEntity (DAO) │  │
│  ├─────────────────────────┤  │
│  │ JournalEntryEntity(DAO) │  │
│  ├─────────────────────────┤  │
│  │ ReadingStreakEntity(DAO)│  │
│  └─────────────────────────┘  │
└───────────────────────────────┘
```

### Data Entities
1. **`PersonalMessage` (Daily Devotional & Love Note)**:
   - `id: Int`, `dayOfYear: Int`, `title: String`, `personalLetter: String` (warm, natural-toned direct message), `scriptureVerse: String`, `scriptureReference: String`, `guidedPrayer: String`, `category: String` (Love & Worth, Peace & Stillness, Courage & Trust, Comfort in Pain), `isBookmarked: Boolean`
2. **`JournalEntry`**:
   - `id: Long`, `title: String`, `content: String`, `category: String`, `timestamp: Long`, `isAnswered: Boolean`, `answeredDate: Long?`, `reflectionNote: String?`
3. **`UserStreak`**:
   - `id: Int = 1`, `currentStreak: Int`, `longestStreak: Int`, `lastDate: String`

---

## 4. Implementation Steps

1. **Step 1 - Branding & Metadata**: Update `metadata.json` and `strings.xml` to "Sanctuary Devotional" with an intimate, inviting description.
2. **Step 2 - Permissions & Manifest**: Configure `POST_NOTIFICATIONS`, `SCHEDULE_EXACT_ALARM`, and register the broadcast receiver.
3. **Step 3 - Room Database & Curated Data**: Build the Room schema and pre-populate with rich, heartfelt devotionals written in an authentic, loving voice.
4. **Step 4 - Voice Recitation Engine**: Implement `SpeechHelper` using Android `TextToSpeech` with pitch and rate tuned for a gentle, calming tone.
5. **Step 5 - Repository & ViewModel**: Implement state management with reactive flows for daily messages, journal entries, and TTS controls.
6. **Step 6 - UI Composables**:
   - `TodayScreen`: Personal letter card, scripture quote, play audio button, breathing exercise modal, guided prayer.
   - `MessagesVaultScreen`: Archive of love notes and private messages by category, plus custom letter writing.
   - `HeartJournalScreen`: Private prayer requests, answered prayer badges, bottom sheet for creating notes.
   - `BreathingModal`: Interactive 4-4-4 rhythm meditation.
   - `SettingsScreen`: Daily reminder alarm picker and theme controls.
7. **Step 7 - Verification**: Compile the applet with `compile_applet` and verify all flows execute without errors.
