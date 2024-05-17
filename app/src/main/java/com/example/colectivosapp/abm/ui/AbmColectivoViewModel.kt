package com.example.colectivosapp.abm.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.colectivosapp.abm.domain.AddColectivoUseCase
import com.example.colectivosapp.abm.domain.GetChoferesUseCase
import com.example.colectivosapp.abm.domain.GetColectivosUseCase
import com.example.colectivosapp.abm.domain.GetLineasUseCase
import com.example.colectivosapp.abm.domain.GetRecorridosUseCase
import com.example.colectivosapp.abm.domain.RemoveColectivoUseCase
import com.example.colectivosapp.abm.ui.model.Chofer
import com.example.colectivosapp.abm.ui.model.Colectivo
import com.example.colectivosapp.abm.ui.model.Linea
import com.example.colectivosapp.abm.ui.model.Recorrido
import com.example.colectivosapp.abm.ui.state.ChoferUiState
import com.example.colectivosapp.abm.ui.state.ColectivoUiState
import com.example.colectivosapp.abm.ui.state.LineaUiState
import com.example.colectivosapp.abm.ui.state.RecorridoUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AbmColectivoViewModel @Inject constructor(
    private val addColectivoUseCase: AddColectivoUseCase,
    private val removeColectivoUseCase: RemoveColectivoUseCase,
    getColectivosUseCase : GetColectivosUseCase,
    getChoferesUseCase : GetChoferesUseCase,
    getLineasUseCase : GetLineasUseCase,
    getRecorridosUseCase : GetRecorridosUseCase
) : ViewModel() {
    private val _selectedRecorrido = MutableLiveData<Recorrido>()
    val selectedRecorrido: MutableLiveData<Recorrido> = _selectedRecorrido
    private val _selectedChofer = MutableLiveData<Chofer>()
    val selectedChofer: MutableLiveData<Chofer> = _selectedChofer
    private val _selectedLinea = MutableLiveData<Linea >()
    val selectedLinea: MutableLiveData<Linea > = _selectedLinea
    private val _patenteCreada = MutableLiveData<String>()
    val patenteCreada : LiveData<String> = _patenteCreada
    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean> = _showDialog
    private val _showConfirmDeleteDialog = MutableLiveData<Boolean>()
    val showConfirmDeleteDialog: LiveData<Boolean> = _showConfirmDeleteDialog
    private val _showMessage = MutableLiveData<Boolean>()
    val showMessage: LiveData<Boolean> = _showMessage
    private val _message = MutableLiveData<String>()
    val message : LiveData<String> = _message
    private val _colectivoSelected: MutableLiveData<Colectivo> = MutableLiveData()
    val colectivoSelected: LiveData<Colectivo> = _colectivoSelected


    val uiStateColectivos: StateFlow<ColectivoUiState> = getColectivosUseCase().map(ColectivoUiState::Success)
        .catch { ColectivoUiState.Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ColectivoUiState.Loading)

    val uiStateChoferes: StateFlow<ChoferUiState> = getChoferesUseCase().map(ChoferUiState::Success)
        .catch { ChoferUiState.Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ChoferUiState.Loading)

    val uiStateRecorridos: StateFlow<RecorridoUiState> = getRecorridosUseCase().map(RecorridoUiState::Success)
        .catch { ChoferUiState.Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), RecorridoUiState.Loading)

    val uiStateLineas: StateFlow<LineaUiState> = getLineasUseCase().map(LineaUiState::Success)
        .catch { LineaUiState.Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), LineaUiState.Loading)
    fun onDialogClose() {
        _showDialog.value = false
    }

    fun onConfirmDialogClose() {
        _showConfirmDeleteDialog.value = false
    }

    fun onColectivoAdded() {
        _showDialog.value = false
        viewModelScope.launch {
            val result = runCatching {
                addColectivoUseCase(
                    Colectivo(
                        patente = patenteCreada.value!!,
                        lineaId = _selectedLinea.value?.id,
                        choferId = _selectedChofer.value?.id,
                        recorridoId = _selectedRecorrido.value?.id))
            }

            result.onSuccess {
                _message.value = "Colectivo agregado exitosamente"
            }

            result.onFailure { error ->
                _message.value = error.message
            }
        }
        _showMessage.value = true
    }

    fun onShowDialogClick() {
        _showDialog.value = true
    }

    fun onShowConfirmDeleteDialogClick(colectivo: Colectivo) {
        _showConfirmDeleteDialog.value = true
        _colectivoSelected.value = colectivo
    }

    fun onMessageDialogClose(){
        _showMessage.value = false
    }

    fun onItemRemove() {
        _showConfirmDeleteDialog.value = false
        viewModelScope.launch(Dispatchers.IO)  {
            removeColectivoUseCase(_colectivoSelected.value!!)
        }
    }

    fun onPatenteChanged(it: String) {
        _patenteCreada.value = it
    }

    fun onChoferChanged(it: Chofer) {
        _selectedChofer.value = it
    }

    fun onLineaChanged(it: Linea) {
        _selectedLinea.value = it
    }

    fun onRecorridoChanged(it: Recorrido) {
        _selectedRecorrido.value = it
    }
}