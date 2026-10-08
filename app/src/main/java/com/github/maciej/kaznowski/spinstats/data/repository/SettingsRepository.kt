package com.github.maciej.kaznowski.spinstats.data.repository

import com.github.maciej.kaznowski.spinstats.data.datastore.SettingsDataStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor(
    private val dataStore: SettingsDataStore
) {
    val apiKey: Flow<String?> = dataStore.apiKey
    val athleteId: Flow<String?> = dataStore.athleteId

    suspend fun saveApiKey(apiKey: String) {
        dataStore.setApiKey(apiKey)
    }

    suspend fun saveAthleteId(athleteId: String) {
        dataStore.setAthleteId(athleteId)
    }
}
