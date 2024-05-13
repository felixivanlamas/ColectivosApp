package com.example.colectivosapp.abm.ui.state

import com.example.colectivosapp.abm.ui.model.Colectivo

sealed interface ColectivoUiState {
    object Loading: ColectivoUiState
    data class Error(val throwable: Throwable): ColectivoUiState
    data class Success(val colectivos:List<Colectivo>): ColectivoUiState
}