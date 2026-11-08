package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Shower
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DailyHabitStatus
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.EmeraldPrimary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DailyHabitTrackerCard(
    habitsMap: Map<String, DailyHabitStatus>,
    onToggleHabit: (dateStr: String, habitType: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val todayDateStr = remember {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }
    val currentHabits = habitsMap[todayDateStr] ?: DailyHabitStatus(dateString = todayDateStr)

    val progress by animateFloatAsState(
        targetValue = currentHabits.completedCount / 5f,
        label = "habit_progress"
    )

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("daily_habits_card")
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Daily Discipline Pillars",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Rewire dopamine with replacement routines",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    color = EmeraldPrimary.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "${currentHabits.completedCount} / 5 Done",
                        color = EmeraldLight,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = { progress },
                color = EmeraldPrimary,
                trackColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            HabitItemRow(
                icon = Icons.Default.Shower,
                title = "Cold Shower or Face Splash",
                subtitle = "Spikes norepinephrine to crush cravings",
                isChecked = currentHabits.coldShower,
                onToggle = { onToggleHabit(todayDateStr, "coldShower") }
            )

            Spacer(modifier = Modifier.height(10.dp))

            HabitItemRow(
                icon = Icons.Default.FitnessCenter,
                title = "20+ Min Physical Workout",
                subtitle = "Releases natural endorphins & burns stress cortisol",
                isChecked = currentHabits.workout,
                onToggle = { onToggleHabit(todayDateStr, "workout") }
            )

            Spacer(modifier = Modifier.height(10.dp))

            HabitItemRow(
                icon = Icons.Default.SelfImprovement,
                title = "4-7-8 Breathing or Meditation",
                subtitle = "Activates prefrontal impulse control",
                isChecked = currentHabits.meditation,
                onToggle = { onToggleHabit(todayDateStr, "meditation") }
            )

            Spacer(modifier = Modifier.height(10.dp))

            HabitItemRow(
                icon = Icons.Default.MenuBook,
                title = "15+ Min Reading or Journaling",
                subtitle = "Trains deep focused attention over cheap clicks",
                isChecked = currentHabits.reading,
                onToggle = { onToggleHabit(todayDateStr, "reading") }
            )

            Spacer(modifier = Modifier.height(10.dp))

            HabitItemRow(
                icon = Icons.Default.NightsStay,
                title = "Screen-Free Bedtime (1 Hr Prior)",
                subtitle = "Locks down the highest-risk relapse zone",
                isChecked = currentHabits.screenFreeBed,
                onToggle = { onToggleHabit(todayDateStr, "screenFreeBed") }
            )
        }
    }
}

@Composable
private fun HabitItemRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    isChecked: Boolean,
    onToggle: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
            .testTag("habit_item_$title")
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isChecked) Icons.Filled.CheckCircle else Icons.Outlined.Circle,
                contentDescription = null,
                tint = if (isChecked) EmeraldPrimary else MaterialTheme.colorScheme.outlineVariant,
                modifier = Modifier.size(22.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isChecked) EmeraldPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = if (isChecked) FontWeight.Bold else FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = if (isChecked) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                )
            }
        }
    }
}
