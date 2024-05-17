package com.example.colectivosapp.abm.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.colectivosapp.abm.ui.model.Chofer
import com.example.colectivosapp.abm.ui.model.ColectivoCompleto
import com.example.colectivosapp.abm.ui.model.Recorrido
import com.example.colectivosapp.abm.ui.myComponents.MyDynamicSelectTextField
import com.example.colectivosapp.abm.ui.myComponents.MyShowError
import com.example.colectivosapp.abm.ui.state.ChoferUiState
import com.example.colectivosapp.abm.ui.state.RecorridoUiState

@Composable
fun ColectivoDetailScreen(
    colectivoId: Int,
    colectivoDetailViewModel: ColectivoDetailViewModel,
    navigationController: NavHostController,
) {
    LaunchedEffect(key1 = colectivoId) {
        colectivoDetailViewModel.getColectivo(colectivoId)
    }
    val selectedColectivo: ColectivoCompleto by colectivoDetailViewModel.selectedColectivo.observeAsState(initial = ColectivoCompleto())
    val selectedChofer: Chofer? by colectivoDetailViewModel.selectedChofer.observeAsState(initial = Chofer())
    val selectedRecorrido: Recorrido? by colectivoDetailViewModel.selectedRecorrido.observeAsState(initial = Recorrido())
    val buttonState: Boolean by colectivoDetailViewModel.buttonState.observeAsState(initial = false)

    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val uiStateChofer by produceState<ChoferUiState>(
        initialValue = ChoferUiState.Loading,
        key1 = lifecycle,
        key2 = colectivoDetailViewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            colectivoDetailViewModel.uiStateChoferes.collect { value = it }
        }
    }

    val uiStateRecorrido by produceState<RecorridoUiState>(
        initialValue = RecorridoUiState.Loading,
        key1 = lifecycle,
        key2 = colectivoDetailViewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            colectivoDetailViewModel.uiStateRecorridos.collect { value = it }
        }
    }

    when (uiStateChofer) {
        is ChoferUiState.Error -> {
            MyShowError((uiStateChofer as ChoferUiState.Error).throwable)
        }

        is ChoferUiState.Loading -> {
            CircularProgressIndicator()
        }

        is ChoferUiState.Success -> {
            Scaffold { it ->
                Box(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize()){

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                            Text(text = "Patente: ${selectedColectivo.patente}", modifier = Modifier.padding(16.dp))
                            Text(text = "${selectedColectivo.linea?.nombre}",modifier = Modifier.padding(16.dp))
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                        MyDynamicSelectTextField<Chofer>(
                            selectedValue = selectedChofer.toString(),
                            options = (uiStateChofer as ChoferUiState.Success).choferes,
                            label = "Chofer asignado",
                            onValueChangedEvent = {colectivoDetailViewModel.selectChofer(it)}
                        )
                        MyDynamicSelectTextField<Recorrido>(
                            selectedValue = selectedRecorrido.toString(),
                            options = (uiStateRecorrido as RecorridoUiState.Success).recorridos,
                            label = "Recorrido asignado",
                            onValueChangedEvent = {colectivoDetailViewModel.selectRecorrido(it)}
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                colectivoDetailViewModel.saveChanges()
                                navigationController.navigateUp()},
                            enabled = buttonState) {
                            Text(text = "Guardar cambios")
                        }
                    }
                }
            }
        }
    }
}