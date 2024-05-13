package com.example.colectivosapp.abm.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.colectivosapp.abm.domain.AddLineaUseCase
import com.example.colectivosapp.abm.domain.GetLineasUseCase
import com.example.colectivosapp.abm.domain.RemoveLineaUseCase
import com.example.colectivosapp.abm.ui.model.Linea
import com.example.colectivosapp.abm.ui.state.LineaUiState
import com.example.colectivosapp.abm.ui.state.LineaUiState.Error
import com.example.colectivosapp.abm.ui.state.LineaUiState.Success
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
class AbmLineaViewModel @Inject constructor(
    private val addLineaUseCase: AddLineaUseCase,
    private val removeLineaUseCase: RemoveLineaUseCase,
    getLineasUseCase: GetLineasUseCase
) : ViewModel() {
    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean> = _showDialog
    private val _showConfirmDeleteDialog = MutableLiveData<Boolean>()
    val showConfirmDeleteDialog: LiveData<Boolean> = _showConfirmDeleteDialog

    var lineaSelected: Linea = Linea(0, "")

    val uiState: StateFlow<LineaUiState> = getLineasUseCase().map(::Success)
        .catch { Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), LineaUiState.Loading)

    fun onDialogClose() {
        _showDialog.value = false
    }

    fun onLineaCreated(nroLinea: String) {
        _showDialog.value = false
        viewModelScope.launch(Dispatchers.IO)  {
            addLineaUseCase(Linea(id = nroLinea.toInt(),nombre = "Linea $nroLinea"))
        }
    }

    fun onShowDialogClick() {
        _showDialog.value = true
    }

    fun onShowConfirmDeleteDialogClick(linea: Linea) {
        _showConfirmDeleteDialog.value = true
        lineaSelected = linea
    }

    fun onConfirmDialogClose() {
        _showConfirmDeleteDialog.value = false
    }

    fun onItemRemove() {
        _showConfirmDeleteDialog.value = false
        viewModelScope.launch (Dispatchers.IO) {
            removeLineaUseCase(lineaSelected)
        }
    }
}