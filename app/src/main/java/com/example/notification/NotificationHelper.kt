package com.example.notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.MainActivity
import com.example.R
import com.example.widget.StreakWidgetProvider
import java.util.Calendar

object NotificationHelper {

    const val CHANNEL_DAILY = "channel_daily_reminders"
    const val CHANNEL_MILESTONES = "channel_milestones"
    const val CHANNEL_URGE_SHIELD = "channel_urge_shield"

    const val NOTIFICATION_ID_DAILY = 1001
    const val NOTIFICATION_ID_EVENING = 1002
    const val NOTIFICATION_ID_MILESTONE = 1003
    const val NOTIFICATION_ID_TEST = 1004

    fun createNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val dailyChannel = NotificationChannel(
                CHANNEL_DAILY,
                "Daily Discipline Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Morning pledge motivation and evening high-risk habit protection"
                enableVibration(true)
            }

            val milestoneChannel = NotificationChannel(
                CHANNEL_MILESTONES,
                "Milestone Celebrations",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Celebrations and biological progress when reaching streak milestones"
                enableVibration(true)
            }

            val urgeChannel = NotificationChannel(
                CHANNEL_URGE_SHIELD,
                "Urge Shield Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Quick emergency intervention when experiencing urges"
                enableVibration(true)
            }

            notificationManager.createNotificationChannels(
                listOf(dailyChannel, milestoneChannel, urgeChannel)
            )
        }
    }

    fun sendTestNotification(context: Context) {
        createNotificationChannels(context)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val shieldIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(StreakWidgetProvider.EXTRA_ACTION, StreakWidgetProvider.ACTION_URGE_SHIELD)
        }
        val shieldPendingIntent = PendingIntent.getActivity(
            context,
            1,
            shieldIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_DAILY)
            .setSmallIcon(R.drawable.app_icon_symbol_1793942241107)
            .setContentTitle("Discipline Guard Active ⚔️")
            .setContentText("Notifications and Urge Shield are primed. Stay focused today!")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Notifications and Urge Shield are primed. Tap below if you ever feel an urge striking, or tap to check your live streak.")
            )
            .setColor(0xFF10B981.toInt())
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .addAction(
                R.drawable.ic_launcher_foreground,
                "Open Urge Shield 🛡️",
                shieldPendingIntent
            )
            .build()

        try {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_TEST, notification)
        } catch (_: SecurityException) {
            // Android 13 permission not yet granted
        }
    }

    fun sendDailyDisciplineNotification(context: Context, isEvening: Boolean) {
        createNotificationChannels(context)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val shieldIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(StreakWidgetProvider.EXTRA_ACTION, StreakWidgetProvider.ACTION_URGE_SHIELD)
        }
        val shieldPendingIntent = PendingIntent.getActivity(
            context,
            2,
            shieldIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val title = if (isEvening) "Evening Night Guard 🛡️" else "Morning Discipline Pledge ⚔️"
        val message = if (isEvening) {
            "Late night is the #1 relapse trigger. Leave your phone away from your bed and protect your clean streak."
        } else {
            "New day, new strength. Tap to lock your discipline pledge for the next 24 hours."
        }

        val notificationId = if (isEvening) NOTIFICATION_ID_EVENING else NOTIFICATION_ID_DAILY

        val notification = NotificationCompat.Builder(context, CHANNEL_DAILY)
            .setSmallIcon(R.drawable.app_icon_symbol_1793942241107)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setColor(0xFF10B981.toInt())
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .addAction(
                R.drawable.ic_launcher_foreground,
                "Urge Shield 🛡️",
                shieldPendingIntent
            )
            .build()

        try {
            NotificationManagerCompat.from(context).notify(notificationId, notification)
        } catch (_: SecurityException) {
            // Permission check
        }
    }

    fun scheduleDailyReminders(context: Context, morningHour: Int = 9, eveningHour: Int = 22) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return

        // Morning Alarm
        scheduleAlarm(context, alarmManager, morningHour, 0, 101, false)

        // Evening Alarm
        scheduleAlarm(context, alarmManager, eveningHour, 30, 102, true)
    }

    private fun scheduleAlarm(
        context: Context,
        alarmManager: AlarmManager,
        hour: Int,
        minute: Int,
        requestCode: Int,
        isEvening: Boolean
    ) {
        val intent = Intent(context, ReminderBroadcastReceiver::class.java).apply {
            putExtra(ReminderBroadcastReceiver.EXTRA_IS_EVENING, isEvening)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            if (before(Calendar.getInstance())) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        try {
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        } catch (_: SecurityException) {
            // Ignored on newer restrictive OS versions if exact alarms restricted
        }
    }

    fun cancelDailyReminders(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val morningIntent = Intent(context, ReminderBroadcastReceiver::class.java)
        val morningPending = PendingIntent.getBroadcast(
            context, 101, morningIntent, PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        morningPending?.let { alarmManager.cancel(it) }

        val eveningIntent = Intent(context, ReminderBroadcastReceiver::class.java)
        val eveningPending = PendingIntent.getBroadcast(
            context, 102, eveningIntent, PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        eveningPending?.let { alarmManager.cancel(it) }
    }
}
