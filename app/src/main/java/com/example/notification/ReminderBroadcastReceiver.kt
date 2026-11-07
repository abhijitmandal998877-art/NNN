package com.example.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.widget.StreakWidgetUpdater

class ReminderBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (action == Intent.ACTION_BOOT_COMPLETED) {
            // Reschedule after reboot and update widgets
            NotificationHelper.scheduleDailyReminders(context)
            StreakWidgetUpdater.updateAllWidgets(context)
            return
        }

        val isEvening = intent.getBooleanExtra(EXTRA_IS_EVENING, false)
        NotificationHelper.sendDailyDisciplineNotification(context, isEvening)

        // Also refresh widget data
        StreakWidgetUpdater.updateAllWidgets(context)
    }

    companion object {
        const val EXTRA_IS_EVENING = "extra_is_evening"
    }
}
