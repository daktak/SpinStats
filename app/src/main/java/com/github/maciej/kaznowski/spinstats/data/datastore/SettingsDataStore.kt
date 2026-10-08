package com.github.maciej.kaznowski.spinstats.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class SettingsDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val API_KEY = stringPreferencesKey("api_key")
        private val ATHLETE_ID = stringPreferencesKey("athlete_id")
    }

    val apiKey: Flow<String?> = context.dataStore.data.map { it[API_KEY] }
    val athleteId: Flow<String?> = context.dataStore.data.map { it[ATHLETE_ID] }

    suspend fun setApiKey(apiKey: String) {
        context.dataStore.edit { prefs ->
            prefs[API_KEY] = apiKey
        }
    }

    suspend fun setAthleteId(athleteId: String) {
        context.dataStore.edit { prefs ->
            prefs[ATHLETE_ID] = athleteId
        }
    }
}
