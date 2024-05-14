package com.example.colectivosapp.abm.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.colectivosapp.abm.domain.GetHistorialUseCase
import com.example.colectivosapp.abm.ui.state.HistorialUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HistorialViewModel @Inject constructor(getHistorialUseCase: GetHistorialUseCase): ViewModel() {
    val uiStateHistorial: StateFlow<HistorialUiState> = getHistorialUseCase().map(HistorialUiState::Success)
        .catch { HistorialUiState.Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HistorialUiState.Loading)
}