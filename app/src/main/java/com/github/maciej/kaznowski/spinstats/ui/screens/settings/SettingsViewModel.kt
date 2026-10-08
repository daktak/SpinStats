package com.github.maciej.kaznowski.spinstats.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.maciej.kaznowski.spinstats.data.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    val apiKey: StateFlow<String?> = settingsRepository.apiKey
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val athleteId: StateFlow<String?> = settingsRepository.athleteId
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun saveSettings(apiKey: String, athleteId: String) {
        viewModelScope.launch {
            if (apiKey.isNotEmpty()) {
                settingsRepository.saveApiKey(apiKey)
            }
            if (athleteId.isNotEmpty()) {
                settingsRepository.saveAthleteId(athleteId)
            }
        }
    }
}
