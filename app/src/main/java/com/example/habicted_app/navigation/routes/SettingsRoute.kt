package com.example.habicted_app.navigation.routes

import SettingsScreen
import android.content.Context
import androidx.compose.runtime.Composable
import com.example.habicted_app.screen.preferences.MainViewModel

@Composable
fun SettingsRoute(mainViewModel: MainViewModel) {
    SettingsScreen(mainViewModel)

}