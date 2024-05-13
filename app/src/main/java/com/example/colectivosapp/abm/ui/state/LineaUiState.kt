package com.example.colectivosapp.abm.ui.state

import com.example.colectivosapp.abm.ui.model.Linea

sealed interface LineaUiState {
    object Loading: LineaUiState
    data class Error(val throwable: Throwable): LineaUiState
    data class Success(val lineas:List<Linea>): LineaUiState
}