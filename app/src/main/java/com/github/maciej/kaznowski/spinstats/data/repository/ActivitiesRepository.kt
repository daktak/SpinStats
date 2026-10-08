package com.github.maciej.kaznowski.spinstats.data.repository

import com.github.maciej.kaznowski.intervalsicuclient.api.ActivitiesApi
import com.github.maciej.kaznowski.intervalsicuclient.model.Activity
import com.github.maciej.kaznowski.intervalsicuclient.model.ActivityStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import android.util.Log
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActivitiesRepository @Inject constructor(
    private val activitiesApi: ActivitiesApi,
    private val settingsRepository: SettingsRepository
) {
    fun getActivities(oldest: String, newest: String): Flow<Result<List<Activity>>> = flow {
        try {
            val athleteId = settingsRepository.athleteId.first()
            if (athleteId.isNullOrBlank()) {
                emit(Result.failure(Exception("Athlete ID not set in settings")))
                return@flow
            }
            val response = withContext(Dispatchers.IO) {
                activitiesApi.listActivities(athleteId, oldest, newest).execute()
            }
            if (response.isSuccessful) {
                emit(Result.success(response.body() ?: emptyList()))
            } else {
                emit(Result.failure(Exception("Failed to fetch activities: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    fun getActivityStreams(activityId: String): Flow<Result<List<ActivityStream>>> = flow {
        try {
            Log.d("ActivitiesRepo", "Fetching streams for activity: $activityId")
            val response = withContext(Dispatchers.IO) {
                activitiesApi.getActivityStreams(activityId).execute()
            }
            Log.d("ActivitiesRepo", "Streams response code: ${response.code()}, successful: ${response.isSuccessful}, body: ${response.body()}")
            if (response.isSuccessful) {
                emit(Result.success(response.body() ?: emptyList()))
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("ActivitiesRepo", "Streams error: ${response.code()} - $errorBody")
                emit(Result.failure(Exception("Failed to fetch streams: ${response.code()} - $errorBody")))
            }
        } catch (e: Exception) {
            Log.e("ActivitiesRepo", "Streams exception", e)
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)
}
