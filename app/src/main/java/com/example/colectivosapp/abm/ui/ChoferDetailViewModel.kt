package com.example.colectivosapp.abm.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.colectivosapp.abm.domain.GetChoferByIdUseCase
import com.example.colectivosapp.abm.domain.GetColectivoByIdUseCase
import com.example.colectivosapp.abm.domain.GetColectivosUseCase
import com.example.colectivosapp.abm.domain.UpdateChoferUseCase
import com.example.colectivosapp.abm.domain.UpdateColectivoUseCase
import com.example.colectivosapp.abm.ui.model.Chofer
import com.example.colectivosapp.abm.ui.model.Colectivo
import com.example.colectivosapp.abm.ui.state.ColectivoUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChoferDetailViewModel @Inject constructor(
    private val getChoferByIdUseCase: GetChoferByIdUseCase,
    private val getColectivoByIdUseCase: GetColectivoByIdUseCase,
    private val updateColectivoUseCase: UpdateColectivoUseCase,
    getColectivosUseCase: GetColectivosUseCase,
    ) : ViewModel() {
    private val _selectedChofer = MutableLiveData<Chofer>()
    val selectedChofer: LiveData<Chofer> = _selectedChofer
    private val _selectedColectivo = MutableLiveData<Colectivo?>()
    val selectedColectivo: MutableLiveData<Colectivo?> = _selectedColectivo
    private val _buttonState = MutableLiveData<Boolean>()
    val buttonState: LiveData<Boolean> = _buttonState
    val uiStateColectivo: StateFlow<ColectivoUiState> = getColectivosUseCase().map(ColectivoUiState::Success)
        .catch { ColectivoUiState.Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ColectivoUiState.Loading)


    fun getChofer(choferId: Int) {
        viewModelScope.launch {
            _selectedChofer.value = getChoferByIdUseCase(choferId)
            if (_selectedChofer.value?.colectivoId != null){
                _selectedColectivo.value = getColectivoByIdUseCase(_selectedChofer.value?.colectivoId!!)
            }
        }
    }

    fun updateChofer() {
        viewModelScope.launch {
            if (verificarCambios()){
                val colectivo = _selectedColectivo.value?.copy(choferId = _selectedChofer.value?.id)
                updateColectivoUseCase(colectivo!!)
            }
            _buttonState.value = false
        }
    }

    fun selectColectivo(it: Colectivo?) {
        _selectedColectivo.value = it
        _buttonState.value = verificarCambios()
    }

    private fun verificarCambios(): Boolean {
        return _selectedChofer.value?.colectivoId != _selectedColectivo.value?.id
    }



}