package com.example.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import com.example.MainActivity
import com.example.R
import com.example.data.MotivationProvider
import com.example.data.repository.StreakRepository
import java.util.concurrent.TimeUnit

class StreakWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == AppWidgetManager.ACTION_APPWIDGET_UPDATE) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val componentName = ComponentName(context, StreakWidgetProvider::class.java)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)
            if (appWidgetIds != null && appWidgetIds.isNotEmpty()) {
                for (id in appWidgetIds) {
                    updateAppWidget(context, appWidgetManager, id)
                }
            }
        }
    }

    companion object {
        const val EXTRA_ACTION = "extra_action"
        const val ACTION_URGE_SHIELD = "action_urge_shield"

        fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val repository = StreakRepository(context)
            val settings = repository.settingsFlow.value
            val now = System.currentTimeMillis()

            val duration = maxOf(0L, now - settings.startTimeMillis)
            val days = TimeUnit.MILLISECONDS.toDays(duration)
            val hours = TimeUnit.MILLISECONDS.toHours(duration) % 24
            val minutes = TimeUnit.MILLISECONDS.toMinutes(duration) % 60

            val bestDays = TimeUnit.MILLISECONDS.toDays(maxOf(settings.bestStreakMillis, duration))

            // Find next target milestone
            var nextMilestoneDays = 1
            for (m in MotivationProvider.MILESTONES) {
                if (days < m.targetDays) {
                    nextMilestoneDays = m.targetDays
                    break
                }
            }

            // Daily quote
            val epochDay = now / (1000 * 60 * 60 * 24)
            val quote = MotivationProvider.getDailyQuote(epochDay)

            val views = RemoteViews(context.packageName, R.layout.widget_streak_layout)
            views.setTextViewText(R.id.widget_streak_days, "$days")
            views.setTextViewText(R.id.widget_streak_time, String.format("%02dh %02dm clean", hours, minutes))
            views.setTextViewText(R.id.widget_milestone_badge, "Next: ${nextMilestoneDays}d")
            views.setTextViewText(R.id.widget_best_record, "Best: ${bestDays}d")
            views.setTextViewText(R.id.widget_quote, "\"${quote.quote}\"")

            // Main app click intent
            val appIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            val appPendingIntent = PendingIntent.getActivity(
                context,
                0,
                appIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_root, appPendingIntent)

            // Urge Shield click intent
            val shieldIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra(EXTRA_ACTION, ACTION_URGE_SHIELD)
            }
            val shieldPendingIntent = PendingIntent.getActivity(
                context,
                1,
                shieldIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_action_btn, shieldPendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
