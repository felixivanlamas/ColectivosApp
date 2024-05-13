package com.example.colectivosapp.abm.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.colectivosapp.abm.domain.AddParadaUseCase
import com.example.colectivosapp.abm.domain.GetParadasUseCase
import com.example.colectivosapp.abm.domain.RemoveParadaUseCase
import com.example.colectivosapp.abm.ui.model.Parada
import com.example.colectivosapp.abm.ui.state.ParadaUiState
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
class AbmParadaViewModel @Inject constructor(
    getParadasUseCase: GetParadasUseCase,
    private val addParadaUseCase: AddParadaUseCase,
    private val removeParadaUseCase: RemoveParadaUseCase) : ViewModel() {

    val uiState: StateFlow<ParadaUiState> = getParadasUseCase().map(ParadaUiState::Success)
        .catch { ParadaUiState.Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ParadaUiState.Loading)

    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean> = _showDialog
    private val _showConfirmDeleteDialog = MutableLiveData<Boolean>()
    val showConfirmDeleteDialog: LiveData<Boolean> = _showConfirmDeleteDialog
    private val _showMessage = MutableLiveData<Boolean>()
    val showMessage: LiveData<Boolean> = _showMessage
    var message: String = ""
    var paradaSelected: Parada = Parada( 0, "", "")


    fun onDialogClose() {
        _showDialog.value = false
    }

    fun onConfirmDialogClose() {
        _showConfirmDeleteDialog.value = false
    }

    fun onParadaCreated(nombre: String, direccion: String) {
        _showDialog.value = false
        viewModelScope.launch(Dispatchers.IO) {
            val result = runCatching {
                addParadaUseCase(
                    Parada(
                        id = 0,
                        nombre = nombre,
                        direccion = direccion
                    )
                )
            }

            result.onSuccess {
                message = "Parada agregada exitosamente"
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

    fun onShowConfirmDeleteDialogClick(parada: Parada) {
        _showConfirmDeleteDialog.value = true
        paradaSelected = parada
    }

    fun onMessageDialogClose() {
        _showMessage.value = false
    }

    fun onItemRemove() {
        _showConfirmDeleteDialog.value = false
        viewModelScope.launch(Dispatchers.IO) {
            removeParadaUseCase(paradaSelected)
        }
    }
}