package com.example.colectivosapp.abm.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.colectivosapp.abm.ui.model.Parada
import com.example.colectivosapp.abm.ui.model.Routes
import com.example.colectivosapp.abm.ui.myComponents.MyConfirmDeleteDialog
import com.example.colectivosapp.abm.ui.myComponents.MyDynamicSelectTextField
import com.example.colectivosapp.abm.ui.myComponents.MyFAB
import com.example.colectivosapp.abm.ui.myComponents.MyMessageDialog
import com.example.colectivosapp.abm.ui.myComponents.MyRecyclerView
import com.example.colectivosapp.abm.ui.myComponents.MyShowError
import com.example.colectivosapp.abm.ui.state.ParadaUiState
import com.example.colectivosapp.abm.ui.state.RecorridoUiState

@Composable
fun AbmRecorridoScreen(
    abmRecorridoViewModel: AbmRecorridoViewModel,
    navigationController: NavHostController
){
    val showDialog: Boolean by abmRecorridoViewModel.showDialog.observeAsState(initial = false)
    val showDeleteDialog: Boolean by abmRecorridoViewModel.showConfirmDeleteDialog.observeAsState(initial = false)
    val showMessage: Boolean by abmRecorridoViewModel.showMessage.observeAsState(initial = false)
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val uiState by produceState<RecorridoUiState>(
        initialValue = RecorridoUiState.Loading,
        key1 = lifecycle,
        key2 = abmRecorridoViewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            abmRecorridoViewModel.uiState.collect { value = it }
        }
    }
    val uiStateParada by produceState<ParadaUiState>(
        initialValue = ParadaUiState.Loading,
        key1 = lifecycle,
        key2 = abmRecorridoViewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            abmRecorridoViewModel.uiStateParadas.collect { value = it }
        }
    }

    when (uiState) {
        is RecorridoUiState.Error -> {
            MyShowError((uiState as RecorridoUiState.Error).throwable)
        }

        is RecorridoUiState.Loading -> {
            CircularProgressIndicator()
        }

        is RecorridoUiState.Success -> {
            Scaffold(
                floatingActionButton = {
                    MyFAB {
                        abmRecorridoViewModel.onShowDialogClick()
                    }
                }
            ) { innerPadding ->
                MyRecyclerView(
                    modifier = Modifier.padding(innerPadding),
                    items = (uiState as RecorridoUiState.Success).recorridos,
                    onItemClick = {item -> navigationController.navigate(Routes.RecorridoDetail.createRoute(item.id))} ,
                    onItemLongPress = {item -> abmRecorridoViewModel.onShowConfirmDeleteDialogClick(item)}
                ) {
                    Text(
                        text = it.toString(),
                        modifier = Modifier.padding(16.dp),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                AddRecorridoDialog(
                    show = showDialog,
                    paradas = (uiStateParada as ParadaUiState.Success).paradas,
                    onDismiss = { abmRecorridoViewModel.onDialogClose() },
                    onRecorridoAdded = { nombre, paradasSeleccionadas ->
                        abmRecorridoViewModel.onRecorridorCreated(nombre, paradasSeleccionadas)
                    }
                )
                MyConfirmDeleteDialog(
                    show = showDeleteDialog,
                    itemToRemove = abmRecorridoViewModel.recorridoSelected.toString(),
                    onDismiss = { abmRecorridoViewModel.onConfirmDialogClose() },
                    onLineaDeleted = { abmRecorridoViewModel.onItemRemove() })
                MyMessageDialog(show = showMessage,message = abmRecorridoViewModel.message, onDismiss = { abmRecorridoViewModel.onMessageDialogClose() }
                )
            }
        }
    }
}


@Composable
fun AddRecorridoDialog(show: Boolean, paradas: List<Parada>, onDismiss: () -> Unit, onRecorridoAdded: (String, List<Int>) -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var paradasSeleccionadas by remember { mutableStateOf(listOf<Parada>()) }
    var index by remember { mutableIntStateOf(0) }
    if (show) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Añadir recorrido",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.align(
                        Alignment.CenterHorizontally
                    )
                )
                Spacer(modifier = Modifier.size(16.dp))
                TextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    singleLine = true,
                    maxLines = 1,
                    label = {
                        Text(text = "Nombre del recorrido")
                    }
                )
                LazyColumn {
                    items(paradasSeleccionadas.size + 1) { index ->
                        Spacer(modifier = Modifier.size(16.dp))
                        Row(Modifier.fillMaxWidth()) {
                            MyDynamicSelectTextField(
                                modifier = Modifier.weight(1f),
                                selectedValue = paradasSeleccionadas.getOrNull(index)?.nombre ?: "",
                                options = paradas.map { it.nombre },
                                label = "Seleccione parada ${index + 1}",
                                onValueChangedEvent = { selectedParada ->
                                    val paradaSeleccionada = paradas.firstOrNull { parada -> parada.nombre == selectedParada }
                                    paradaSeleccionada?.let { parada ->
                                        paradasSeleccionadas = paradasSeleccionadas.toMutableList().apply {
                                            if (index < size) {
                                                set(index, parada)
                                            } else {
                                                add(parada)
                                            }
                                        }
                                    }
                                },
                            )
                            if (index < paradasSeleccionadas.size) {
                                IconButton(
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically),
                                    onClick = {
                                        paradasSeleccionadas = paradasSeleccionadas.toMutableList().apply {
                                            removeAt(index)
                                        }
                                    }
                                ) {
                                    Icon(Icons.Default.Close, contentDescription = "Borrar parada")
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.size(16.dp))
                Button(onClick = {
                    onRecorridoAdded(nombre, paradasSeleccionadas.map { it.id })
                    nombre = ""
                    paradasSeleccionadas = emptyList()
                    index = 0
                }, Modifier.fillMaxWidth()) {
                    Text(text = "Añadir")
                }
            }
        }
    }
}