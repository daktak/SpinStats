package com.github.maciej.kaznowski.spinstats.ui.screens.activities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.maciej.kaznowski.intervalsicuclient.model.Activity
import com.github.maciej.kaznowski.spinstats.data.repository.ActivitiesRepository
import com.github.maciej.kaznowski.spinstats.data.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

data class ActivitiesUiState(
    val activities: List<Activity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ActivitiesViewModel @Inject constructor(
    private val activitiesRepository: ActivitiesRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ActivitiesUiState())
    val uiState: StateFlow<ActivitiesUiState> = _uiState.asStateFlow()

    init {
        loadActivities()
    }

    fun refresh() {
        loadActivities()
    }

    private fun loadActivities() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val calendar = Calendar.getInstance()
            val newest = dateFormat.format(calendar.time)
            calendar.add(Calendar.MONTH, -3)
            val oldest = dateFormat.format(calendar.time)
            activitiesRepository.getActivities(oldest, newest)
                .onEach { result ->
                    result.onSuccess { activities ->
                        _uiState.value = _uiState.value.copy(
                            activities = activities,
                            isLoading = false,
                            error = null
                        )
                    }.onFailure { throwable ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = throwable.message ?: "Unknown error"
                        )
                    }
                }
                .launchIn(viewModelScope)
        }
    }
}
