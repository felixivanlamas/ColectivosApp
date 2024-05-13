package com.example.colectivosapp.abm.ui.state

import com.example.colectivosapp.abm.ui.model.Parada

interface ParadaUiState {
    object Loading: ParadaUiState
    data class Error(val throwable: Throwable): ParadaUiState
    data class Success(val paradas:List<Parada>): ParadaUiState
}