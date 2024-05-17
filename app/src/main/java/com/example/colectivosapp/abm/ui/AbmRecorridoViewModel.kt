package com.example.colectivosapp.abm.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.colectivosapp.abm.domain.AddRecorridoUseCase
import com.example.colectivosapp.abm.domain.GetParadasUseCase
import com.example.colectivosapp.abm.domain.GetRecorridosUseCase
import com.example.colectivosapp.abm.domain.RemoveRecorridoUseCase
import com.example.colectivosapp.abm.ui.model.Recorrido
import com.example.colectivosapp.abm.ui.state.ParadaUiState
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
class AbmRecorridoViewModel @Inject constructor(
    getRecorridosUseCase: GetRecorridosUseCase,
    private val addRecorridoUseCase: AddRecorridoUseCase,
    private val removeRecorridoUseCase: RemoveRecorridoUseCase,
    getParadasUseCase: GetParadasUseCase
):ViewModel() {
    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean> = _showDialog
    private val _showConfirmDeleteDialog = MutableLiveData<Boolean>()
    val showConfirmDeleteDialog: LiveData<Boolean> = _showConfirmDeleteDialog
    private val _showMessage = MutableLiveData<Boolean>()
    val showMessage: LiveData<Boolean> = _showMessage
    var message: String = ""
    var recorridoSelected: Recorrido = Recorrido(0, "")

    val uiStateParadas: StateFlow<ParadaUiState> = getParadasUseCase().map(ParadaUiState::Success)
        .catch { ParadaUiState.Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ParadaUiState.Loading)

    val uiState: StateFlow<RecorridoUiState> = getRecorridosUseCase().map(RecorridoUiState::Success)
        .catch { RecorridoUiState.Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), RecorridoUiState.Loading)

    fun onDialogClose() {
        _showDialog.value = false
    }

    fun onConfirmDialogClose() {
        _showConfirmDeleteDialog.value = false
    }

    fun onRecorridorCreated(nombre: String, paradasIds: List<Int>) {
        _showDialog.value = false
        viewModelScope.launch(Dispatchers.IO) {
            val result = runCatching {
                addRecorridoUseCase(
                    Recorrido(
                        nombre = nombre
                    ),
                    paradasIds = paradasIds
                )
            }

            result.onSuccess {
                message = "Recorrido agregado exitosamente"
            }

            result.onFailure { error ->
                message = error.message.toString()
            }
        }
        _showMessage.value = true
    }

    fun onShowDialogClick() {
        _showDialog.value = true
    }

    fun onShowConfirmDeleteDialogClick(recorrido: Recorrido) {
        recorridoSelected = recorrido
        _showConfirmDeleteDialog.value = true
    }

    fun onMessageDialogClose() {
        _showMessage.value = false
    }

    fun onItemRemove() {
        _showConfirmDeleteDialog.value = false
        viewModelScope.launch(Dispatchers.IO) {
            removeRecorridoUseCase(recorridoSelected)
        }
    }
}