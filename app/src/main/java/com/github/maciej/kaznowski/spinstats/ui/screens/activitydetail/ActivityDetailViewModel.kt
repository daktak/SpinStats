package com.github.maciej.kaznowski.spinstats.ui.screens.activitydetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.maciej.kaznowski.intervalsicuclient.model.ActivityStream
import com.github.maciej.kaznowski.spinstats.data.repository.ActivitiesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

data class ActivityDetailUiState(
    val streams: List<ActivityStream> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ActivityDetailViewModel @Inject constructor(
    private val activitiesRepository: ActivitiesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ActivityDetailUiState())
    val uiState: StateFlow<ActivityDetailUiState> = _uiState.asStateFlow()

    fun loadStreams(activityId: String) {
        if (activityId.isBlank()) return
        Log.d("ActivityDetailVM", "Loading streams for activity: $activityId")
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        activitiesRepository.getActivityStreams(activityId)
            .onEach { result ->
                result.onSuccess { streams ->
                    Log.d("ActivityDetailVM", "Got streams: \${streams.size}")
                    _uiState.value = _uiState.value.copy(
                        streams = streams,
                        isLoading = false,
                        error = null
                    )
                }.onFailure { throwable ->
                    Log.e("ActivityDetailVM", "Stream error", throwable)
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = throwable.message ?: "Unknown error"
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun refresh(activityId: String) {
        loadStreams(activityId)
    }
}
