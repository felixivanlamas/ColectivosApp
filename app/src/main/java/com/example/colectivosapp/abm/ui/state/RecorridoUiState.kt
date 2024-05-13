package com.example.colectivosapp.abm.ui.state

import com.example.colectivosapp.abm.ui.model.Recorrido

interface RecorridoUiState {
    object Loading: RecorridoUiState
    data class Error(val throwable: Throwable): RecorridoUiState
    data class Success(val recorridos:List<Recorrido>): RecorridoUiState
}