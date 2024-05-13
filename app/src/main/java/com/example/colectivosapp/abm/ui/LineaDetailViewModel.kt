package com.example.colectivosapp.abm.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.colectivosapp.abm.domain.AddColectivoUseCase
import com.example.colectivosapp.abm.domain.GetColectivosByLineaIdUseCase
import com.example.colectivosapp.abm.domain.RemoveColectivoFromLineaUseCase
import com.example.colectivosapp.abm.ui.model.Colectivo
import com.example.colectivosapp.abm.ui.state.ColectivoUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LineaDetailViewModel @Inject constructor(
    getColectivosByLineaIdUseCase: GetColectivosByLineaIdUseCase,
    private val addColectivoUseCase: AddColectivoUseCase,
    private val removeColectivoFromLineaUseCase: RemoveColectivoFromLineaUseCase
) : ViewModel() {


    private val _showConfirmDeleteDialog = MutableLiveData<Boolean>()
    val showConfirmDeleteDialog: LiveData<Boolean> = _showConfirmDeleteDialog
    private val _lineaId = MutableStateFlow(0)
    val lineaId: MutableStateFlow<Int> = _lineaId

    var colectivoSelected : Colectivo= Colectivo(id = 0, patente = "")

    private var _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean> = _showDialog

    var message: String=""

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: Flow<ColectivoUiState> = _lineaId.flatMapLatest { lineaId ->
        getColectivosByLineaIdUseCase(lineaId)
            .map { colectivos ->
                if (colectivos.isNotEmpty()) {
                    ColectivoUiState.Success(colectivos)
                } else {
                    ColectivoUiState.Error(Exception("No se encontraron colectivos para la línea con ID $lineaId"))
                }
            }
            .catch { error ->
                ColectivoUiState.Error(error)
            }
            .stateIn(viewModelScope)
    }

    fun onShowDialogClick() {
        _showDialog.value = true
    }

    fun onDialogClose() {
        _showDialog.value = false
    }

    fun onShowConfirmDeleteDialogClick(colectivo: Colectivo) {
        _showConfirmDeleteDialog.value = true
        colectivoSelected = colectivo
    }

    fun onConfirmDialogClose() {
        _showConfirmDeleteDialog.value = false
    }

    fun removeColectivo() {
        _showConfirmDeleteDialog.value = false
        viewModelScope.launch(Dispatchers.IO)  {
            removeColectivoFromLineaUseCase(colectivoSelected)
        }
    }

    private fun setLineaId(lineaId: Int) {
        _lineaId.value = lineaId
    }

    fun init(lineaId: Int) {
        setLineaId(lineaId)
    }

    fun onColectivoCreated(patente: String, lineaId: Int) {
        _showDialog.value = false
        viewModelScope.launch(Dispatchers.IO) {
            val result = runCatching {
                addColectivoUseCase(Colectivo(patente = patente, lineaId = lineaId))
            }

            result.onSuccess {
                message = "Colectivo agregado exitosamente"
            }

            result.onFailure { error ->
                message = error.message.toString()
            }
        }
    }
}