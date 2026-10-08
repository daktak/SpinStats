package com.github.maciej.kaznowski.spinstats

import com.github.maciej.kaznowski.intervalsicuclient.api.ActivitiesApi
import com.github.maciej.kaznowski.intervalsicuclient.api.AthletesApi
import com.github.maciej.kaznowski.intervalsicuclient.invoker.auth.HttpBasicAuth
import com.github.maciej.kaznowski.intervalsicuclient.invoker.infrastructure.ApiClient
import com.github.maciej.kaznowski.spinstats.data.repository.SettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class IntervalsIcuClientModule {

    @Provides
    @Singleton
    fun providesApiClient(settingsRepository: SettingsRepository): ApiClient {
        val apiKey = runBlocking { settingsRepository.apiKey.first() }
        val client = ApiClient(baseUrl = "https://intervals.icu")
        if (!apiKey.isNullOrBlank()) {
            client.addAuthorization("basic", HttpBasicAuth("API_KEY", apiKey))
        }
        return client
    }

    @Provides
    fun providesActivitiesApi(apiClient: ApiClient): ActivitiesApi {
        return apiClient.createService(ActivitiesApi::class.java)
    }

    @Provides
    fun providesAthletesApi(apiClient: ApiClient): AthletesApi {
        return apiClient.createService(AthletesApi::class.java)
    }
}
