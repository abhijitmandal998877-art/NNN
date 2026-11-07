package com.example.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build

object StreakWidgetUpdater {

    fun updateAllWidgets(context: Context) {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val componentName = ComponentName(context, StreakWidgetProvider::class.java)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)
        if (appWidgetIds != null && appWidgetIds.isNotEmpty()) {
            for (id in appWidgetIds) {
                StreakWidgetProvider.updateAppWidget(context, appWidgetManager, id)
            }
        }
    }

    fun isPinSupported(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            appWidgetManager.isRequestPinAppWidgetSupported
        } else {
            false
        }
    }

    fun requestPinWidget(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            if (appWidgetManager.isRequestPinAppWidgetSupported) {
                val componentName = ComponentName(context, StreakWidgetProvider::class.java)
                return appWidgetManager.requestPinAppWidget(componentName, null, null)
            }
        }
        return false
    }
}
