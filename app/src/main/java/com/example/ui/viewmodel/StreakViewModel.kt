package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.MotivationProvider
import com.example.data.model.DailyQuote
import com.example.data.model.Milestone
import com.example.data.model.RelapseEvent
import com.example.data.model.StreakSettings
import com.example.data.repository.StreakRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

data class LiveStreakUiState(
    val days: Long = 0,
    val hours: Long = 0,
    val minutes: Long = 0,
    val seconds: Long = 0,
    val totalDurationMillis: Long = 0,
    val bestStreakDurationMillis: Long = 0,
    val totalDaysTracked: Long = 1,
    val relapsesCount: Int = 0,
    val currentMilestone: Milestone = MotivationProvider.MILESTONES[0],
    val currentMilestoneProgress: Float = 0f,
    val daysToNextMilestone: Long = 1,
    val nextMilestoneTargetDays: Int = 1,
    val isPledgedToday: Boolean = false,
    val activeQuote: DailyQuote = MotivationProvider.QUOTES[0],
    val themeMode: String = "DARK",
    val supportiveMessage: String? = null
)

class StreakViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = StreakRepository(application)

    private val _currentTime = MutableStateFlow(System.currentTimeMillis())
    val currentTime: StateFlow<Long> = _currentTime.asStateFlow()

    private val _quoteIndex = MutableStateFlow(0)
    val quoteIndex: StateFlow<Int> = _quoteIndex.asStateFlow()

    private val _supportiveMessage = MutableStateFlow<String?>(null)
    val supportiveMessage: StateFlow<String?> = _supportiveMessage.asStateFlow()

    val settings: StateFlow<StreakSettings> = repository.settingsFlow
    val relapses: StateFlow<List<RelapseEvent>> = repository.relapsesFlow
    val pledges: StateFlow<Set<String>> = repository.pledgesFlow

    init {
        // Live second ticker
        viewModelScope.launch {
            while (true) {
                _currentTime.value = System.currentTimeMillis()
                delay(1000)
            }
        }
        // Initialize daily quote based on epoch day
        val epochDay = System.currentTimeMillis() / (1000 * 60 * 60 * 24)
        _quoteIndex.value = (Math.abs(epochDay) % MotivationProvider.QUOTES.size).toInt()
    }

    private val streakCoreFlow = combine(
        settings,
        _currentTime,
        relapses,
        pledges
    ) { settingsVal, now, relapsesVal, pledgesVal ->
        val duration = maxOf(0L, now - settingsVal.startTimeMillis)
        val days = TimeUnit.MILLISECONDS.toDays(duration)
        val hours = TimeUnit.MILLISECONDS.toHours(duration) % 24
        val minutes = TimeUnit.MILLISECONDS.toMinutes(duration) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(duration) % 60

        val bestDuration = maxOf(settingsVal.bestStreakMillis, duration)
        val trackedTotalMillis = maxOf(duration, now - settingsVal.firstStartedMillis)
        val totalDaysTracked = maxOf(1L, TimeUnit.MILLISECONDS.toDays(trackedTotalMillis) + 1)

        val milestones = MotivationProvider.MILESTONES
        var activeMilestone = milestones.last()
        var prevMilestoneDays = 0
        var nextTargetDays = milestones.first().targetDays
        var progress = 1f

        for (i in milestones.indices) {
            val m = milestones[i]
            if (days < m.targetDays) {
                activeMilestone = m
                nextTargetDays = m.targetDays
                prevMilestoneDays = if (i > 0) milestones[i - 1].targetDays else 0
                val span = (m.targetDays - prevMilestoneDays).toFloat()
                val currentSpan = (days - prevMilestoneDays).toFloat() + (hours / 24f) + (minutes / 1440f)
                progress = (currentSpan / span).coerceIn(0f, 1f)
                break
            }
        }

        val daysToNext = maxOf(0L, nextTargetDays - days)
        val todayStr = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date(now))
        val isPledged = pledgesVal.contains(todayStr)

        LiveStreakUiState(
            days = days,
            hours = hours,
            minutes = minutes,
            seconds = seconds,
            totalDurationMillis = duration,
            bestStreakDurationMillis = bestDuration,
            totalDaysTracked = totalDaysTracked,
            relapsesCount = relapsesVal.size,
            currentMilestone = activeMilestone,
            currentMilestoneProgress = progress,
            daysToNextMilestone = daysToNext,
            nextMilestoneTargetDays = nextTargetDays,
            isPledgedToday = isPledged,
            themeMode = settingsVal.themeMode
        )
    }

    val uiState: StateFlow<LiveStreakUiState> = combine(
        streakCoreFlow,
        _quoteIndex,
        _supportiveMessage
    ) { core, qIdx, suppMsg ->
        val quote = MotivationProvider.QUOTES[qIdx % MotivationProvider.QUOTES.size]
        core.copy(
            activeQuote = quote,
            supportiveMessage = suppMsg
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        LiveStreakUiState()
    )

    fun recordRelapse(triggerReason: String, note: String) {
        val completedDuration = repository.recordRelapse(triggerReason, note)
        val days = TimeUnit.MILLISECONDS.toDays(completedDuration)
        val hours = TimeUnit.MILLISECONDS.toHours(completedDuration) % 24

        val message = if (days > 0) {
            "You achieved $days day${if (days > 1) "s" else ""} $hours hr before this setback. That progress is NOT erased! Your brain made genuine rewiring steps. Take a deep breath, forgive yourself, and stand back up today."
        } else {
            "A setback is just data, not defeat. Be gentle with yourself. Identify what triggered you, wash your face, and start your clean streak right now."
        }
        _supportiveMessage.value = message
    }

    fun dismissSupportiveMessage() {
        _supportiveMessage.value = null
    }

    fun updateStartTime(newStartTime: Long) {
        repository.updateStartTime(newStartTime)
    }

    fun setThemeMode(mode: String) {
        repository.updateThemeMode(mode)
    }

    fun recordPledge(): Boolean {
        return repository.recordPledge()
    }

    fun nextQuote() {
        _quoteIndex.value = (_quoteIndex.value + 1) % MotivationProvider.QUOTES.size
    }

    fun resetAllData() {
        repository.resetAllData()
        _supportiveMessage.value = null
    }

    fun exportJson(): String {
        return repository.exportToJson()
    }

    fun importJson(json: String): Result<Unit> {
        val result = repository.importFromJson(json)
        if (result.isSuccess) {
            _supportiveMessage.value = "Progress imported successfully!"
        }
        return result
    }
}
