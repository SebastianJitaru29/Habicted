package com.example.habicted_app.screen.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

val Context.myPreferencesDataStore: DataStore<Preferences> by preferencesDataStore("settings")

enum class NetworkPreference {
    WIFI,
    ANY,
    High
}

data class TaskStatus(
    val isLight: Boolean,
    val netpreference: NetworkPreference
)

@Singleton
class MyPreferencesDataStore @Inject constructor(
    @ApplicationContext context: Context
){
    private val myPreferencesDataStore = context.myPreferencesDataStore

    private object PreferencesKeys{
        val IS_COMPLETED_KEY = booleanPreferencesKey("is_completed")
        val PRIORITY_KEY = stringPreferencesKey("priority")
    }

    val taskStatusFlow = myPreferencesDataStore.data
        .catch { exception ->
            if (exception is IOException){
                emit(emptyPreferences())
            }else{
                throw exception
            }
        }.map { preferences ->
            val isCompleted = preferences[PreferencesKeys.IS_COMPLETED_KEY] ?: false
            val priority = NetworkPreference.valueOf(
                preferences[PreferencesKeys.PRIORITY_KEY] ?: NetworkPreference.WIFI.name
            )
            TaskStatus(isCompleted, priority)
        }

    suspend fun updateIsCompleted(isCompleted: Boolean){
        myPreferencesDataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_COMPLETED_KEY] = isCompleted
        }
    }

    suspend fun updateNetworkPreference(priority: NetworkPreference){
        myPreferencesDataStore.edit { preferences ->
            preferences[PreferencesKeys.PRIORITY_KEY] = priority.name
        }
    }
}
