package com.example.colectivosapp.abm.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.colectivosapp.abm.domain.GetChoferesUseCase
import com.example.colectivosapp.abm.domain.GetColectivoByIdUseCase
import com.example.colectivosapp.abm.domain.GetRecorridosUseCase
import com.example.colectivosapp.abm.domain.UpdateColectivoUseCase
import com.example.colectivosapp.abm.ui.model.Chofer
import com.example.colectivosapp.abm.ui.model.Colectivo
import com.example.colectivosapp.abm.ui.model.ColectivoCompleto
import com.example.colectivosapp.abm.ui.model.Recorrido
import com.example.colectivosapp.abm.ui.state.ChoferUiState
import com.example.colectivosapp.abm.ui.state.RecorridoUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ColectivoDetailViewModel @Inject constructor(
    private val getColectivoByIdUseCase: GetColectivoByIdUseCase,
    private val updateColectivoUseCase: UpdateColectivoUseCase,
    getChoferesUseCase: GetChoferesUseCase,
    getRecorridosUseCase: GetRecorridosUseCase
):ViewModel() {
    private val _buttonState = MutableLiveData<Boolean>()
    val buttonState: LiveData<Boolean> = _buttonState
    private val _selectedRecorrido = MutableLiveData<Recorrido?>()
    val selectedRecorrido: MutableLiveData<Recorrido?> = _selectedRecorrido
    private val _selectedChofer = MutableLiveData<Chofer?>()
    val selectedChofer: MutableLiveData<Chofer?> = _selectedChofer
    private val _selectedColectivo = MutableLiveData<ColectivoCompleto>()
    val selectedColectivo: LiveData<ColectivoCompleto> = _selectedColectivo

    val uiStateChoferes: StateFlow<ChoferUiState> = getChoferesUseCase().map(ChoferUiState::Success)
        .catch { ChoferUiState.Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ChoferUiState.Loading)

    val uiStateRecorridos: StateFlow<RecorridoUiState> = getRecorridosUseCase().map(RecorridoUiState::Success)
        .catch { ChoferUiState.Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), RecorridoUiState.Loading)

    fun getColectivo(colectivoId: Int) {
        viewModelScope.launch {
            _selectedColectivo.value = getColectivoByIdUseCase(colectivoId)
            _selectedChofer.value = _selectedColectivo.value?.chofer
            _selectedRecorrido.value = _selectedColectivo.value?.recorrido
        }
    }

    fun selectChofer(choferSeleccionado: Chofer?) {
        _selectedChofer.value = choferSeleccionado
        _buttonState.value = verificarCambios()
    }

    fun selectRecorrido(recorridoSeleccionado: Recorrido?) {
        _selectedRecorrido.value = recorridoSeleccionado
        _buttonState.value = verificarCambios()
    }

    fun saveChanges() {
        viewModelScope.launch {
            updateColectivoUseCase(
                Colectivo(
                    id = _selectedColectivo.value?.id!!,
                    patente = _selectedColectivo.value?.patente!!,
                    lineaId = _selectedColectivo.value?.linea!!.id,
                    choferId = _selectedChofer.value?.id,
                    recorridoId = _selectedRecorrido.value?.id))
        }
    }
    private fun verificarCambios():Boolean{
        return verificarCambioChofer() || verificarCambioRecorrido()
    }

    private fun verificarCambioChofer():Boolean{
        return _selectedColectivo.value?.chofer?.id != _selectedChofer.value?.id
    }

    private fun verificarCambioRecorrido():Boolean{
        return _selectedColectivo.value?.recorrido?.id != _selectedRecorrido.value?.id
    }
}