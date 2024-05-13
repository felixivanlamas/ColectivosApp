package com.example.colectivosapp.abm.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.colectivosapp.abm.domain.AddPasajeroUseCase
import com.example.colectivosapp.abm.ui.model.Pasajero
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasajeroRegisterViewModel @Inject constructor(private val addPasajeroUseCase: AddPasajeroUseCase) : ViewModel() {
    var nombrePasajero by  mutableStateOf("")
        private set
    var apellidoPasajero by  mutableStateOf("")
        private set
    var dniPasajero by  mutableStateOf("")
        private set

    fun updateNombrePasajero(input: String) {
        nombrePasajero = input
    }

    fun updateApellidoPasajero(input: String) {
        apellidoPasajero = input
    }

    fun updateDniPasajero(input: String) {
        dniPasajero = input
    }

    private fun cleanInputs() {
        nombrePasajero =""
        apellidoPasajero = ""
        dniPasajero = ""
    }

    fun addPasajero() {
        viewModelScope.launch(Dispatchers.IO) {
            addPasajeroUseCase(Pasajero(pasajeroId = dniPasajero.toInt(), nombrePasajero, apellidoPasajero, dniPasajero))
            cleanInputs()
        }
    }
}