package com.example.colectivosapp.abm.ui.state

import com.example.colectivosapp.abm.ui.model.RowDataHistorial

interface HistorialUiState {
    object Loading: HistorialUiState
    data class Error(val throwable: Throwable): HistorialUiState
    data class Success(val rowDataHistorial:List<RowDataHistorial>): HistorialUiState
}