package com.example.colectivosapp.abm.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.colectivosapp.abm.domain.AddColectivoUseCase
import com.example.colectivosapp.abm.domain.GetColectivosUseCase
import com.example.colectivosapp.abm.domain.RemoveColectivoUseCase
import com.example.colectivosapp.abm.ui.model.Colectivo
import com.example.colectivosapp.abm.ui.state.ColectivoUiState
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
    getColectivosUseCase : GetColectivosUseCase
) : ViewModel() {
    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean> = _showDialog
    private val _showConfirmDeleteDialog = MutableLiveData<Boolean>()
    val showConfirmDeleteDialog: LiveData<Boolean> = _showConfirmDeleteDialog
    private val _showMessage = MutableLiveData<Boolean>()
    val showMessage: LiveData<Boolean> = _showMessage
    var message: String=""
    var colectivoSelected: Colectivo = Colectivo(0, "", 0, 0, 0)

    val uiState: StateFlow<ColectivoUiState> = getColectivosUseCase().map(ColectivoUiState::Success)
        .catch { ColectivoUiState.Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ColectivoUiState.Loading)

    fun onDialogClose() {
        _showDialog.value = false
    }

    fun onConfirmDialogClose() {
        _showConfirmDeleteDialog.value = false
    }

    fun onColectivoCreated(patente: String, lineaId: Int) {
        _showDialog.value = false
        viewModelScope.launch(Dispatchers.IO) {
            val result = runCatching {
                addColectivoUseCase(Colectivo(patente = patente, lineaId = lineaId, choferId = null, recorridoId = null))
            }

            result.onSuccess {
                message = "Colectivo agregado exitosamente"
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

    fun onShowConfirmDeleteDialogClick(colectivo: Colectivo) {
        _showConfirmDeleteDialog.value = true
        colectivoSelected = colectivo
    }

    fun onMessageDialogClose(){
        _showMessage.value = false
    }

    fun onItemRemove() {
        _showConfirmDeleteDialog.value = false
        viewModelScope.launch(Dispatchers.IO)  {
            removeColectivoUseCase(colectivoSelected)
        }
    }
}