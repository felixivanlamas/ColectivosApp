package com.example.colectivosapp.abm.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import com.example.colectivosapp.abm.ui.model.Chofer
import com.example.colectivosapp.abm.ui.model.Linea
import com.example.colectivosapp.abm.ui.model.Recorrido
import com.example.colectivosapp.abm.ui.model.Routes
import com.example.colectivosapp.abm.ui.myComponents.MyConfirmDeleteDialog
import com.example.colectivosapp.abm.ui.myComponents.MyDynamicSelectTextField
import com.example.colectivosapp.abm.ui.myComponents.MyFAB
import com.example.colectivosapp.abm.ui.myComponents.MyMessageDialog
import com.example.colectivosapp.abm.ui.myComponents.MyRecyclerView
import com.example.colectivosapp.abm.ui.myComponents.MyShowError
import com.example.colectivosapp.abm.ui.state.ChoferUiState
import com.example.colectivosapp.abm.ui.state.ColectivoUiState
import com.example.colectivosapp.abm.ui.state.LineaUiState
import com.example.colectivosapp.abm.ui.state.RecorridoUiState

@Composable
fun AbmColectivoScreen(
    abmColectivoViewModel: AbmColectivoViewModel,
    navigationController: NavHostController
) {
    val showDialog: Boolean by abmColectivoViewModel.showDialog.observeAsState(initial = false)
    val showDeleteDialog: Boolean by abmColectivoViewModel.showConfirmDeleteDialog.observeAsState(initial = false)
    val showMessage: Boolean by abmColectivoViewModel.showMessage.observeAsState(initial = false)
    val message: String by abmColectivoViewModel.message.observeAsState(initial = "")
    val patenteCreada: String by abmColectivoViewModel.patenteCreada.observeAsState("")
    val selectedLinea: Linea by abmColectivoViewModel.selectedLinea.observeAsState(initial = Linea())
    val selectedChofer:Chofer by abmColectivoViewModel.selectedChofer.observeAsState(initial = Chofer())
    val selectedRecorrido:Recorrido by abmColectivoViewModel.selectedRecorrido.observeAsState(initial = Recorrido())

    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val uiStateColectivos by produceState<ColectivoUiState>(
        initialValue = ColectivoUiState.Loading,
        key1 = lifecycle,
        key2 = abmColectivoViewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            abmColectivoViewModel.uiStateColectivos.collect { value = it }
        }
    }

    val uiStateLineas by produceState<LineaUiState>(
        initialValue = LineaUiState.Loading,
        key1 = lifecycle,
        key2 = abmColectivoViewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            abmColectivoViewModel.uiStateLineas.collect { value = it }
        }
    }

    val uiStateChoferes by produceState<ChoferUiState>(
        initialValue = ChoferUiState.Loading,
        key1 = lifecycle,
        key2 = abmColectivoViewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            abmColectivoViewModel.uiStateChoferes.collect { value = it }
        }
    }

    val uiStateRecorridos by produceState<RecorridoUiState>(
        initialValue = RecorridoUiState.Loading,
        key1 = lifecycle,
        key2 = abmColectivoViewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            abmColectivoViewModel.uiStateRecorridos.collect { value = it }
        }
    }

    when (uiStateColectivos) {
        is ColectivoUiState.Error -> {
            MyShowError((uiStateColectivos as ColectivoUiState.Error).throwable)
        }

        is ColectivoUiState.Loading -> {
            CircularProgressIndicator()
        }

        is ColectivoUiState.Success -> {
            Scaffold(
                floatingActionButton = {
                    MyFAB {
                        abmColectivoViewModel.onShowDialogClick()
                    }
                }
            ) { innerPadding ->
                MyRecyclerView(
                    modifier = Modifier.padding(innerPadding),
                    items= (uiStateColectivos as ColectivoUiState.Success).colectivos,
                    onItemClick = {navigationController.navigate(Routes.ColectivoDetail.createRoute(it.id))},
                    onItemLongPress = {abmColectivoViewModel.onShowConfirmDeleteDialogClick(it)})
                {
                    Text(
                        text = it.toString(),
                        modifier = Modifier
                            .padding(16.dp),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                AddColectivoDialog(
                    show = showDialog,
                    patenteCreada = patenteCreada,
                    selectedChofer = selectedChofer,
                    selectedLinea = selectedLinea,
                    selectedRecorrido = selectedRecorrido,
                    choferes = (uiStateChoferes as ChoferUiState.Success).choferes,
                    lineas = (uiStateLineas as LineaUiState.Success).lineas,
                    recorridos = (uiStateRecorridos as RecorridoUiState.Success).recorridos,
                    onDismiss = { abmColectivoViewModel.onDialogClose() },
                    onPatenteChanged = { abmColectivoViewModel.onPatenteChanged(it) },
                    onChoferChanged = { abmColectivoViewModel.onChoferChanged(it) },
                    onLineaChanged = { abmColectivoViewModel.onLineaChanged(it) },
                    onRecorridoChanged = { abmColectivoViewModel.onRecorridoChanged(it) },
                    onColectivoAdded = { abmColectivoViewModel.onColectivoAdded() },
                )
                MyConfirmDeleteDialog(
                    show = showDeleteDialog,
                    itemToRemove = abmColectivoViewModel.colectivoSelected.value,
                    onDismiss = { abmColectivoViewModel.onConfirmDialogClose() },
                    onDeleted = { abmColectivoViewModel.onItemRemove() })
                MyMessageDialog(show = showMessage,message = message, onDismiss = { abmColectivoViewModel.onMessageDialogClose() }
                )
            }
        }
    }
}

@Composable
fun AddColectivoDialog(
    show: Boolean,
    patenteCreada: String,
    selectedLinea: Linea,
    selectedChofer:Chofer ,
    selectedRecorrido:Recorrido ,
    choferes: List<Chofer>,
    lineas: List<Linea>,
    recorridos: List<Recorrido> ,
    onDismiss: () -> Unit,
    onPatenteChanged: (String) -> Unit ,
    onChoferChanged: (Chofer) -> Unit ,
    onLineaChanged: (Linea) -> Unit ,
    onRecorridoChanged: (Recorrido) -> Unit ,
    onColectivoAdded: () -> Unit) {
    if (show) {
        Box(contentAlignment = Alignment.Center){
            Dialog(onDismissRequest = { onDismiss() }) {
                Column(
                    verticalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Añadir colectivo",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.align(
                            Alignment.CenterHorizontally
                        )
                    )
                    TextField( value = patenteCreada, onValueChange = { onPatenteChanged(it) }, label = { Text("Patente") })
                    MyDynamicSelectTextField(
                        selectedValue = selectedLinea.toString(),
                        options = lineas,
                        label = "Seleccione una linea",
                        onValueChangedEvent = {
                            if (it != null) {
                                onLineaChanged(it)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    MyDynamicSelectTextField(
                        selectedValue = selectedChofer.toString(),
                        options = choferes,
                        label = "Seleccione un chofer",
                        onValueChangedEvent = {
                            if (it != null) {
                                onChoferChanged(it)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    MyDynamicSelectTextField(
                        selectedValue = selectedRecorrido.toString(),
                        options = recorridos,
                        label = "Seleccione un recorrido",
                        onValueChangedEvent = {
                            if (it != null) {
                                onRecorridoChanged(it)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(onClick = {
                        onColectivoAdded()
                    }, Modifier.fillMaxWidth()) {
                        Text(text = "Añadir")
                    }
                }
            }
        }

    }
}