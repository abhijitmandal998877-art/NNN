package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.DailyHabitStatus
import com.example.data.model.RecoveryJournalEntry
import com.example.data.model.RelapseEvent
import com.example.data.model.StreakSettings
import com.example.data.model.UrgeEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class StreakRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("stop_masturbate_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_START_TIME = "key_start_time"
        private const val KEY_BEST_STREAK = "key_best_streak"
        private const val KEY_FIRST_STARTED = "key_first_started"
        private const val KEY_THEME_MODE = "key_theme_mode"
        private const val KEY_RELAPSES_JSON = "key_relapses_json"
        private const val KEY_PLEDGES_JSON = "key_pledges_json"
        private const val KEY_URGES_JSON = "key_urges_json"
        private const val KEY_HABITS_JSON = "key_habits_json"
        private const val KEY_JOURNAL_JSON = "key_journal_json"
        private const val KEY_DAILY_REMINDERS = "key_daily_reminders"
        private const val KEY_EVENING_GUARD = "key_evening_guard"
        private const val KEY_MILESTONE_ALERTS = "key_milestone_alerts"
    }

    private val _settingsFlow = MutableStateFlow(loadSettings())
    val settingsFlow: StateFlow<StreakSettings> = _settingsFlow.asStateFlow()

    private val _relapsesFlow = MutableStateFlow(loadRelapses())
    val relapsesFlow: StateFlow<List<RelapseEvent>> = _relapsesFlow.asStateFlow()

    private val _pledgesFlow = MutableStateFlow(loadPledges())
    val pledgesFlow: StateFlow<Set<String>> = _pledgesFlow.asStateFlow()

    private val _urgesFlow = MutableStateFlow(loadUrges())
    val urgesFlow: StateFlow<List<UrgeEvent>> = _urgesFlow.asStateFlow()

    private val _habitsFlow = MutableStateFlow(loadHabits())
    val habitsFlow: StateFlow<Map<String, DailyHabitStatus>> = _habitsFlow.asStateFlow()

    private val _journalFlow = MutableStateFlow(loadJournal())
    val journalFlow: StateFlow<List<RecoveryJournalEntry>> = _journalFlow.asStateFlow()

    private fun loadSettings(): StreakSettings {
        val now = System.currentTimeMillis()
        var startTime = prefs.getLong(KEY_START_TIME, -1L)
        var firstStarted = prefs.getLong(KEY_FIRST_STARTED, -1L)
        val bestStreak = prefs.getLong(KEY_BEST_STREAK, 0L)
        val themeMode = prefs.getString(KEY_THEME_MODE, "DARK") ?: "DARK"
        val dailyReminders = prefs.getBoolean(KEY_DAILY_REMINDERS, true)
        val eveningGuard = prefs.getBoolean(KEY_EVENING_GUARD, true)
        val milestoneAlerts = prefs.getBoolean(KEY_MILESTONE_ALERTS, true)

        if (startTime <= 0L) {
            startTime = now
            prefs.edit().putLong(KEY_START_TIME, startTime).apply()
        }
        if (firstStarted <= 0L) {
            firstStarted = startTime
            prefs.edit().putLong(KEY_FIRST_STARTED, firstStarted).apply()
        }

        return StreakSettings(
            startTimeMillis = startTime,
            bestStreakMillis = bestStreak,
            firstStartedMillis = firstStarted,
            themeMode = themeMode,
            dailyRemindersEnabled = dailyReminders,
            eveningGuardEnabled = eveningGuard,
            milestoneAlertsEnabled = milestoneAlerts
        )
    }

    private fun loadRelapses(): List<RelapseEvent> {
        val jsonStr = prefs.getString(KEY_RELAPSES_JSON, null) ?: return emptyList()
        val list = mutableListOf<RelapseEvent>()
        try {
            val jsonArray = JSONArray(jsonStr)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                list.add(
                    RelapseEvent(
                        id = obj.optString("id", UUID.randomUUID().toString()),
                        timestampMillis = obj.optLong("timestampMillis", System.currentTimeMillis()),
                        streakDurationMillis = obj.optLong("streakDurationMillis", 0L),
                        triggerReason = obj.optString("triggerReason", "Not specified"),
                        note = obj.optString("note", "")
                    )
                )
            }
        } catch (_: Exception) {}
        return list.sortedByDescending { it.timestampMillis }
    }

    private fun loadPledges(): Set<String> {
        val jsonStr = prefs.getString(KEY_PLEDGES_JSON, null) ?: return emptySet()
        val set = mutableSetOf<String>()
        try {
            val arr = JSONArray(jsonStr)
            for (i in 0 until arr.length()) {
                set.add(arr.getString(i))
            }
        } catch (_: Exception) {}
        return set
    }

    private fun loadUrges(): List<UrgeEvent> {
        val jsonStr = prefs.getString(KEY_URGES_JSON, null) ?: return emptyList()
        val list = mutableListOf<UrgeEvent>()
        try {
            val arr = JSONArray(jsonStr)
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                list.add(
                    UrgeEvent(
                        id = obj.optString("id", UUID.randomUUID().toString()),
                        timestampMillis = obj.optLong("timestampMillis", System.currentTimeMillis()),
                        intensity = obj.optInt("intensity", 5),
                        trigger = obj.optString("trigger", "Not specified"),
                        techniqueUsed = obj.optString("techniqueUsed", "Urge Surfing"),
                        note = obj.optString("note", "")
                    )
                )
            }
        } catch (_: Exception) {}
        return list.sortedByDescending { it.timestampMillis }
    }

    private fun loadHabits(): Map<String, DailyHabitStatus> {
        val jsonStr = prefs.getString(KEY_HABITS_JSON, null) ?: return emptyMap()
        val map = mutableMapOf<String, DailyHabitStatus>()
        try {
            val arr = JSONArray(jsonStr)
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                val dateStr = obj.getString("dateString")
                map[dateStr] = DailyHabitStatus(
                    dateString = dateStr,
                    coldShower = obj.optBoolean("coldShower", false),
                    workout = obj.optBoolean("workout", false),
                    meditation = obj.optBoolean("meditation", false),
                    reading = obj.optBoolean("reading", false),
                    screenFreeBed = obj.optBoolean("screenFreeBed", false)
                )
            }
        } catch (_: Exception) {}
        return map
    }

    private fun loadJournal(): List<RecoveryJournalEntry> {
        val jsonStr = prefs.getString(KEY_JOURNAL_JSON, null) ?: return emptyList()
        val list = mutableListOf<RecoveryJournalEntry>()
        try {
            val arr = JSONArray(jsonStr)
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                list.add(
                    RecoveryJournalEntry(
                        id = obj.optString("id", UUID.randomUUID().toString()),
                        timestampMillis = obj.optLong("timestampMillis", System.currentTimeMillis()),
                        mood = obj.optString("mood", "Strong 💪"),
                        note = obj.optString("note", "")
                    )
                )
            }
        } catch (_: Exception) {}
        return list.sortedByDescending { it.timestampMillis }
    }

    private fun saveRelapses(list: List<RelapseEvent>) {
        val arr = JSONArray()
        list.forEach { event ->
            val obj = JSONObject().apply {
                put("id", event.id)
                put("timestampMillis", event.timestampMillis)
                put("streakDurationMillis", event.streakDurationMillis)
                put("triggerReason", event.triggerReason)
                put("note", event.note)
            }
            arr.put(obj)
        }
        prefs.edit().putString(KEY_RELAPSES_JSON, arr.toString()).apply()
        _relapsesFlow.value = list.sortedByDescending { it.timestampMillis }
    }

    private fun saveUrges(list: List<UrgeEvent>) {
        val arr = JSONArray()
        list.forEach { event ->
            val obj = JSONObject().apply {
                put("id", event.id)
                put("timestampMillis", event.timestampMillis)
                put("intensity", event.intensity)
                put("trigger", event.trigger)
                put("techniqueUsed", event.techniqueUsed)
                put("note", event.note)
            }
            arr.put(obj)
        }
        prefs.edit().putString(KEY_URGES_JSON, arr.toString()).apply()
        _urgesFlow.value = list.sortedByDescending { it.timestampMillis }
    }

    private fun saveHabits(map: Map<String, DailyHabitStatus>) {
        val arr = JSONArray()
        map.values.forEach { habit ->
            val obj = JSONObject().apply {
                put("dateString", habit.dateString)
                put("coldShower", habit.coldShower)
                put("workout", habit.workout)
                put("meditation", habit.meditation)
                put("reading", habit.reading)
                put("screenFreeBed", habit.screenFreeBed)
            }
            arr.put(obj)
        }
        prefs.edit().putString(KEY_HABITS_JSON, arr.toString()).apply()
        _habitsFlow.value = map
    }

    private fun saveJournal(list: List<RecoveryJournalEntry>) {
        val arr = JSONArray()
        list.forEach { entry ->
            val obj = JSONObject().apply {
                put("id", entry.id)
                put("timestampMillis", entry.timestampMillis)
                put("mood", entry.mood)
                put("note", entry.note)
            }
            arr.put(obj)
        }
        prefs.edit().putString(KEY_JOURNAL_JSON, arr.toString()).apply()
        _journalFlow.value = list.sortedByDescending { it.timestampMillis }
    }

    fun updateStartTime(newStartTime: Long) {
        val current = _settingsFlow.value
        val now = System.currentTimeMillis()
        val validStartTime = if (newStartTime > now) now else newStartTime
        val currentStreak = now - validStartTime
        val newBest = maxOf(current.bestStreakMillis, currentStreak)

        val updated = current.copy(
            startTimeMillis = validStartTime,
            bestStreakMillis = newBest
        )
        prefs.edit()
            .putLong(KEY_START_TIME, validStartTime)
            .putLong(KEY_BEST_STREAK, newBest)
            .apply()
        _settingsFlow.value = updated
    }

    fun recordRelapse(triggerReason: String, note: String): Long {
        val now = System.currentTimeMillis()
        val current = _settingsFlow.value
        val completedStreakDuration = maxOf(0L, now - current.startTimeMillis)
        val newBest = maxOf(current.bestStreakMillis, completedStreakDuration)

        val relapseEvent = RelapseEvent(
            id = UUID.randomUUID().toString(),
            timestampMillis = now,
            streakDurationMillis = completedStreakDuration,
            triggerReason = triggerReason.ifBlank { "Unspecified" },
            note = note.trim()
        )

        val updatedList = listOf(relapseEvent) + _relapsesFlow.value
        saveRelapses(updatedList)

        val updatedSettings = current.copy(
            startTimeMillis = now,
            bestStreakMillis = newBest
        )
        prefs.edit()
            .putLong(KEY_START_TIME, now)
            .putLong(KEY_BEST_STREAK, newBest)
            .apply()
        _settingsFlow.value = updatedSettings

        return completedStreakDuration
    }

    fun recordUrgeConquered(intensity: Int, trigger: String, technique: String, note: String): UrgeEvent {
        val now = System.currentTimeMillis()
        val urge = UrgeEvent(
            id = UUID.randomUUID().toString(),
            timestampMillis = now,
            intensity = intensity.coerceIn(1, 10),
            trigger = trigger.ifBlank { "Urge Conquered" },
            techniqueUsed = technique.ifBlank { "Urge Surfing" },
            note = note.trim()
        )
        val updated = listOf(urge) + _urgesFlow.value
        saveUrges(updated)
        return urge
    }

    fun toggleHabit(dateString: String, habitType: String) {
        val map = _habitsFlow.value.toMutableMap()
        val current = map[dateString] ?: DailyHabitStatus(dateString = dateString)
        val updated = when (habitType) {
            "coldShower" -> current.copy(coldShower = !current.coldShower)
            "workout" -> current.copy(workout = !current.workout)
            "meditation" -> current.copy(meditation = !current.meditation)
            "reading" -> current.copy(reading = !current.reading)
            "screenFreeBed" -> current.copy(screenFreeBed = !current.screenFreeBed)
            else -> current
        }
        map[dateString] = updated
        saveHabits(map)
    }

    fun addJournalEntry(mood: String, note: String): RecoveryJournalEntry {
        val now = System.currentTimeMillis()
        val entry = RecoveryJournalEntry(
            id = UUID.randomUUID().toString(),
            timestampMillis = now,
            mood = mood.ifBlank { "Strong 💪" },
            note = note.trim()
        )
        val updated = listOf(entry) + _journalFlow.value
        saveJournal(updated)
        return entry
    }

    fun updateThemeMode(themeMode: String) {
        val updated = _settingsFlow.value.copy(themeMode = themeMode)
        prefs.edit().putString(KEY_THEME_MODE, themeMode).apply()
        _settingsFlow.value = updated
    }

    fun updateNotificationSettings(daily: Boolean, evening: Boolean, milestone: Boolean) {
        val updated = _settingsFlow.value.copy(
            dailyRemindersEnabled = daily,
            eveningGuardEnabled = evening,
            milestoneAlertsEnabled = milestone
        )
        prefs.edit()
            .putBoolean(KEY_DAILY_REMINDERS, daily)
            .putBoolean(KEY_EVENING_GUARD, evening)
            .putBoolean(KEY_MILESTONE_ALERTS, milestone)
            .apply()
        _settingsFlow.value = updated
    }

    fun recordPledge(): Boolean {
        val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val current = _pledgesFlow.value
        if (current.contains(todayStr)) return false

        val updated = current + todayStr
        val arr = JSONArray()
        updated.forEach { arr.put(it) }
        prefs.edit().putString(KEY_PLEDGES_JSON, arr.toString()).apply()
        _pledgesFlow.value = updated
        return true
    }

    fun resetAllData() {
        val now = System.currentTimeMillis()
        prefs.edit().clear().apply()

        prefs.edit()
            .putLong(KEY_START_TIME, now)
            .putLong(KEY_BEST_STREAK, 0L)
            .putLong(KEY_FIRST_STARTED, now)
            .putString(KEY_THEME_MODE, "DARK")
            .apply()

        _settingsFlow.value = StreakSettings(
            startTimeMillis = now,
            bestStreakMillis = 0L,
            firstStartedMillis = now,
            themeMode = "DARK"
        )
        _relapsesFlow.value = emptyList()
        _pledgesFlow.value = emptySet()
        _urgesFlow.value = emptyList()
        _habitsFlow.value = emptyMap()
        _journalFlow.value = emptyList()
    }

    fun exportToJson(): String {
        val settings = _settingsFlow.value
        val relapses = _relapsesFlow.value
        val pledges = _pledgesFlow.value
        val urges = _urgesFlow.value
        val habits = _habitsFlow.value
        val journal = _journalFlow.value

        val root = JSONObject()
        root.put("app", "Stop Masturbate")
        root.put("version", 2)
        root.put("exportedAt", System.currentTimeMillis())
        root.put("startTimeMillis", settings.startTimeMillis)
        root.put("bestStreakMillis", settings.bestStreakMillis)
        root.put("firstStartedMillis", settings.firstStartedMillis)
        root.put("themeMode", settings.themeMode)

        val relapsesArr = JSONArray()
        relapses.forEach {
            val item = JSONObject().apply {
                put("id", it.id)
                put("timestampMillis", it.timestampMillis)
                put("streakDurationMillis", it.streakDurationMillis)
                put("triggerReason", it.triggerReason)
                put("note", it.note)
            }
            relapsesArr.put(item)
        }
        root.put("relapses", relapsesArr)

        val pledgesArr = JSONArray()
        pledges.forEach { pledgesArr.put(it) }
        root.put("pledges", pledgesArr)

        val urgesArr = JSONArray()
        urges.forEach {
            val item = JSONObject().apply {
                put("id", it.id)
                put("timestampMillis", it.timestampMillis)
                put("intensity", it.intensity)
                put("trigger", it.trigger)
                put("techniqueUsed", it.techniqueUsed)
                put("note", it.note)
            }
            urgesArr.put(item)
        }
        root.put("urges", urgesArr)

        val habitsArr = JSONArray()
        habits.values.forEach {
            val item = JSONObject().apply {
                put("dateString", it.dateString)
                put("coldShower", it.coldShower)
                put("workout", it.workout)
                put("meditation", it.meditation)
                put("reading", it.reading)
                put("screenFreeBed", it.screenFreeBed)
            }
            habitsArr.put(item)
        }
        root.put("habits", habitsArr)

        val journalArr = JSONArray()
        journal.forEach {
            val item = JSONObject().apply {
                put("id", it.id)
                put("timestampMillis", it.timestampMillis)
                put("mood", it.mood)
                put("note", it.note)
            }
            journalArr.put(item)
        }
        root.put("journal", journalArr)

        return root.toString(2)
    }

    fun importFromJson(jsonString: String): Result<Unit> {
        return try {
            val root = JSONObject(jsonString)
            val startTime = root.getLong("startTimeMillis")
            val bestStreak = root.optLong("bestStreakMillis", 0L)
            val firstStarted = root.optLong("firstStartedMillis", startTime)
            val themeMode = root.optString("themeMode", "DARK")

            val relapsesList = mutableListOf<RelapseEvent>()
            if (root.has("relapses")) {
                val relapsesArr = root.getJSONArray("relapses")
                for (i in 0 until relapsesArr.length()) {
                    val obj = relapsesArr.getJSONObject(i)
                    relapsesList.add(
                        RelapseEvent(
                            id = obj.optString("id", UUID.randomUUID().toString()),
                            timestampMillis = obj.getLong("timestampMillis"),
                            streakDurationMillis = obj.optLong("streakDurationMillis", 0L),
                            triggerReason = obj.optString("triggerReason", "Imported"),
                            note = obj.optString("note", "")
                        )
                    )
                }
            }

            val pledgesSet = mutableSetOf<String>()
            if (root.has("pledges")) {
                val pledgesArr = root.getJSONArray("pledges")
                for (i in 0 until pledgesArr.length()) {
                    pledgesSet.add(pledgesArr.getString(i))
                }
            }

            val urgesList = mutableListOf<UrgeEvent>()
            if (root.has("urges")) {
                val urgesArr = root.getJSONArray("urges")
                for (i in 0 until urgesArr.length()) {
                    val obj = urgesArr.getJSONObject(i)
                    urgesList.add(
                        UrgeEvent(
                            id = obj.optString("id", UUID.randomUUID().toString()),
                            timestampMillis = obj.optLong("timestampMillis", System.currentTimeMillis()),
                            intensity = obj.optInt("intensity", 5),
                            trigger = obj.optString("trigger", "Imported"),
                            techniqueUsed = obj.optString("techniqueUsed", "Urge Surfing"),
                            note = obj.optString("note", "")
                        )
                    )
                }
            }

            val habitsMap = mutableMapOf<String, DailyHabitStatus>()
            if (root.has("habits")) {
                val habitsArr = root.getJSONArray("habits")
                for (i in 0 until habitsArr.length()) {
                    val obj = habitsArr.getJSONObject(i)
                    val dateStr = obj.getString("dateString")
                    habitsMap[dateStr] = DailyHabitStatus(
                        dateString = dateStr,
                        coldShower = obj.optBoolean("coldShower", false),
                        workout = obj.optBoolean("workout", false),
                        meditation = obj.optBoolean("meditation", false),
                        reading = obj.optBoolean("reading", false),
                        screenFreeBed = obj.optBoolean("screenFreeBed", false)
                    )
                }
            }

            val journalList = mutableListOf<RecoveryJournalEntry>()
            if (root.has("journal")) {
                val journalArr = root.getJSONArray("journal")
                for (i in 0 until journalArr.length()) {
                    val obj = journalArr.getJSONObject(i)
                    journalList.add(
                        RecoveryJournalEntry(
                            id = obj.optString("id", UUID.randomUUID().toString()),
                            timestampMillis = obj.optLong("timestampMillis", System.currentTimeMillis()),
                            mood = obj.optString("mood", "Strong 💪"),
                            note = obj.optString("note", "")
                        )
                    )
                }
            }

            // Save to prefs
            prefs.edit()
                .putLong(KEY_START_TIME, startTime)
                .putLong(KEY_BEST_STREAK, bestStreak)
                .putLong(KEY_FIRST_STARTED, firstStarted)
                .putString(KEY_THEME_MODE, themeMode)
                .apply()

            saveRelapses(relapsesList)
            saveUrges(urgesList)
            saveHabits(habitsMap)
            saveJournal(journalList)

            val pledgesArr = JSONArray()
            pledgesSet.forEach { pledgesArr.put(it) }
            prefs.edit().putString(KEY_PLEDGES_JSON, pledgesArr.toString()).apply()

            _settingsFlow.value = StreakSettings(
                startTimeMillis = startTime,
                bestStreakMillis = bestStreak,
                firstStartedMillis = firstStarted,
                themeMode = themeMode
            )
            _pledgesFlow.value = pledgesSet
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
