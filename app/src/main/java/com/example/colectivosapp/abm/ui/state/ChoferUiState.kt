package com.example.colectivosapp.abm.ui.state

import com.example.colectivosapp.abm.ui.model.Chofer

sealed interface ChoferUiState {
    object Loading: ChoferUiState
    data class Error(val throwable: Throwable): ChoferUiState
    data class Success(val choferes:List<Chofer>): ChoferUiState
}