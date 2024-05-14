package com.example.colectivosapp.abm.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.colectivosapp.abm.domain.AddHistorialUseCase
import com.example.colectivosapp.abm.domain.GetPasajerosUseCase
import com.example.colectivosapp.abm.domain.GetRecorridoByIdUseCase
import com.example.colectivosapp.abm.domain.GetRecorridosUseCase
import com.example.colectivosapp.abm.ui.model.Historial
import com.example.colectivosapp.abm.ui.model.ParadaOrden
import com.example.colectivosapp.abm.ui.model.Pasajero
import com.example.colectivosapp.abm.ui.model.Recorrido
import com.example.colectivosapp.abm.ui.state.PasajeroUiState
import com.example.colectivosapp.abm.ui.state.RecorridoUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class SimulacionViewModel @Inject constructor(
    getPasajerosUseCase: GetPasajerosUseCase,
    getRecorridosUseCase: GetRecorridosUseCase,
    private val getRecorridoByIdUseCase: GetRecorridoByIdUseCase ,
    private val addHistorialUseCase: AddHistorialUseCase) : ViewModel() {

    companion object{
       private const val TIEMPOENPARADA : Long = 30000
    }
    private var _showSimulacion = MutableLiveData<Boolean>()
    val showSimulacion: LiveData<Boolean> = _showSimulacion
    private var _pasajeroSeleccionado = MutableLiveData<Pasajero?>()
    val pasajeroSeleccionado: MutableLiveData<Pasajero?> = _pasajeroSeleccionado
    private var _recorridoSeleccionado = MutableLiveData<Recorrido?>()
    val recorridoSeleccionado: MutableLiveData<Recorrido?> = _recorridoSeleccionado
    private val _paradasRecorridoSeleccionado = MutableLiveData<List<ParadaOrden>?>()
    val paradasRecorridoSeleccionado: MutableLiveData<List<ParadaOrden>?> = _paradasRecorridoSeleccionado
    private val _paradaActual = MutableLiveData<Int?>()
    val paradaActual: MutableLiveData<Int?> = _paradaActual
    private var _paradaSubida = MutableLiveData<Int?>()
    val paradaSubida : MutableLiveData<Int?> = _paradaSubida

    val uiStatePasajeros: StateFlow<PasajeroUiState> = getPasajerosUseCase().map(PasajeroUiState::Success)
        .catch { PasajeroUiState.Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PasajeroUiState.Loading)

    val uiStateRecorridos: StateFlow<RecorridoUiState> = getRecorridosUseCase().map(RecorridoUiState::Success)
        .catch { RecorridoUiState.Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), RecorridoUiState.Loading)


    fun onShowSimulacionClick() {
        _showSimulacion.value = true
    }

    private fun onSimulacionClose() {
        _showSimulacion.value = false
    }

    fun onPasajeroChange(it: Pasajero) {
        _pasajeroSeleccionado.value = it
    }

    fun onRecorridoChange(it: Recorrido) {
        _recorridoSeleccionado.value = it
    }

    fun onBackClick() {
        onSimulacionClose()
    }



    fun iniciarSimulacion() {
        _showSimulacion.value = false
        viewModelScope.launch{
            _paradasRecorridoSeleccionado.value = getRecorridoByIdUseCase(recorridoSeleccionado.value!!.id).paradas
            if (paradasRecorridoSeleccionado.value!!.isNotEmpty()){
                iniciarRecorrido()
            }
        }
    }

    private fun iniciarRecorrido() {
        viewModelScope.launch {
            _paradaActual.value = 0
            val paradasRecorrido = _paradasRecorridoSeleccionado.value ?: return@launch
            while (_paradaActual.value != null && _paradaActual.value!! <= (paradasRecorrido.size - 1)) {
                delay(TIEMPOENPARADA)
                _paradaActual.value?.let { currentValue ->
                    _paradaActual.value = currentValue + 1
                }
            }
        }
    }

    fun subirPasajero() {
        _paradaSubida.value = _paradasRecorridoSeleccionado.value!![_paradaActual.value!!].parada.id
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun bajarPasajero() {
        viewModelScope.launch {
            if (_pasajeroSeleccionado.value != null) {
                val fechaActual = LocalDate.now()
                addHistorialUseCase(
                    Historial(
                    pasajeroId = _pasajeroSeleccionado.value!!.pasajeroId,
                    subeId = _pasajeroSeleccionado.value!!.subeId,
                    paradaSubidaId = _paradaSubida.value!!,
                    paradaBajadaId = _paradasRecorridoSeleccionado.value!![_paradaActual.value!!].parada.id,
                    fecha = fechaActual.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                )
            }
            clear()
        }
    }


    private fun clear(){
        _showSimulacion.value = false
        _pasajeroSeleccionado.value = null
        _recorridoSeleccionado.value = null
        _paradasRecorridoSeleccionado.value = null
        _paradaActual.value = null
        _paradaSubida.value = null
    }
}