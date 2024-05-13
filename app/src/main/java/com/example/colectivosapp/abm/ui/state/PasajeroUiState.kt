package com.example.colectivosapp.abm.ui.state

import com.example.colectivosapp.abm.ui.model.Pasajero

interface PasajeroUiState {
    object Loading: PasajeroUiState
    data class Error(val throwable: Throwable): PasajeroUiState
    data class Success(val pasajeros:List<Pasajero>): PasajeroUiState
}