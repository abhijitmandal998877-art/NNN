package com.example

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.ui.screens.MainScreen
import com.example.ui.viewmodel.StreakViewModel
import com.example.widget.StreakWidgetProvider

class MainActivity : ComponentActivity() {
    private val streakViewModel: StreakViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleIntent(intent)
        enableEdgeToEdge()
        setContent {
            MainScreen(viewModel = streakViewModel)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        if (intent?.getStringExtra(StreakWidgetProvider.EXTRA_ACTION) == StreakWidgetProvider.ACTION_URGE_SHIELD) {
            streakViewModel.triggerUrgeShieldFromExternal()
        }
    }
}
