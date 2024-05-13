package com.example.colectivosapp.abm.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.colectivosapp.abm.domain.AddChoferUseCase
import com.example.colectivosapp.abm.domain.GetChoferesUseCase
import com.example.colectivosapp.abm.domain.RemoveChoferUseCase
import com.example.colectivosapp.abm.ui.model.Chofer
import com.example.colectivosapp.abm.ui.state.ChoferUiState
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
class AbmChoferScreenViewModel @Inject constructor(
    private val addChoferUseCase: AddChoferUseCase,
    private val removeChoferUseCase: RemoveChoferUseCase,
    getChoferesUseCase: GetChoferesUseCase
) : ViewModel() {
    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean> = _showDialog
    private val _showConfirmDeleteDialog = MutableLiveData<Boolean>()
    val showConfirmDeleteDialog: LiveData<Boolean> = _showConfirmDeleteDialog
    private val _showMessage = MutableLiveData<Boolean>()
    val showMessage: LiveData<Boolean> = _showMessage
    var message: String= ""
    var choferSelected: Chofer = Chofer(0, "","","" )

    val uiState: StateFlow<ChoferUiState> = getChoferesUseCase().map(ChoferUiState::Success)
        .catch { ChoferUiState.Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ChoferUiState.Loading)

    fun onDialogClose() {
        _showDialog.value = false
    }

    fun onConfirmDialogClose() {
        _showConfirmDeleteDialog.value = false
    }

    fun onChoferCreated(nombre: String, apellido: String, documento:String) {
        _showDialog.value = false
        viewModelScope.launch (Dispatchers.IO){
            val result = runCatching {
                addChoferUseCase(Chofer(id = documento.toInt(), nombre = nombre, apellido = apellido, documento = documento))
            }

            result.onSuccess {
                message = "Chofer agregado exitosamente"
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

    fun onShowConfirmDeleteDialogClick(chofer: Chofer) {
        choferSelected = chofer
        _showConfirmDeleteDialog.value = true
    }

    fun onMessageDialogClose(){
        _showMessage.value = false
    }

    fun onItemRemove() {
        _showConfirmDeleteDialog.value = false
        viewModelScope.launch (Dispatchers.IO){
            removeChoferUseCase(choferSelected)
        }
    }
}