package com.example.data.model

data class RelapseEvent(
    val id: String,
    val timestampMillis: Long,
    val streakDurationMillis: Long,
    val triggerReason: String = "Not specified",
    val note: String = ""
)

data class StreakSettings(
    val startTimeMillis: Long,
    val bestStreakMillis: Long,
    val firstStartedMillis: Long,
    val themeMode: String = "DARK", // DARK, LIGHT, SYSTEM
    val dailyRemindersEnabled: Boolean = true,
    val eveningGuardEnabled: Boolean = true,
    val milestoneAlertsEnabled: Boolean = true
)

data class Milestone(
    val targetDays: Int,
    val title: String,
    val badgeLabel: String,
    val description: String,
    val biologicalBenefit: String
)

data class DailyQuote(
    val quote: String,
    val author: String,
    val context: String
)

data class DisciplineTip(
    val id: String,
    val title: String,
    val summary: String,
    val category: String, // "Urge Shield", "Dopamine Science", "Habit Building", "Mental Clarity"
    val actionItem: String
)

data class RecoveryMilestonePhase(
    val days: String,
    val phaseName: String,
    val brainRecovery: String,
    val mentalShift: String
)
