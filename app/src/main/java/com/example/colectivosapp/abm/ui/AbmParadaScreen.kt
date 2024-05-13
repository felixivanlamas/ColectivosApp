package com.example.colectivosapp.abm.ui

import androidx.compose.foundation.background
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
import com.example.colectivosapp.abm.ui.model.Routes
import com.example.colectivosapp.abm.ui.myComponents.MyConfirmDeleteDialog
import com.example.colectivosapp.abm.ui.myComponents.MyFAB
import com.example.colectivosapp.abm.ui.myComponents.MyMessageDialog
import com.example.colectivosapp.abm.ui.myComponents.MyRecyclerView
import com.example.colectivosapp.abm.ui.myComponents.MyShowError
import com.example.colectivosapp.abm.ui.state.ParadaUiState

@Composable
fun AbmParadaScreen(
    abmParadaViewModel: AbmParadaViewModel,
    navigationController: NavHostController
) {
    val showDialog: Boolean by abmParadaViewModel.showDialog.observeAsState(initial = false)
    val showDeleteDialog: Boolean by abmParadaViewModel.showConfirmDeleteDialog.observeAsState(initial = false)
    val showMessage: Boolean by abmParadaViewModel.showMessage.observeAsState(initial = false)
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val uiState by produceState<ParadaUiState>(
        initialValue = ParadaUiState.Loading,
        key1 = lifecycle,
        key2 = abmParadaViewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            abmParadaViewModel.uiState.collect { value = it }
        }
    }

    when (uiState) {
        is ParadaUiState.Error -> {
            MyShowError((uiState as ParadaUiState.Error).throwable)
        }

        is ParadaUiState.Loading -> {
            CircularProgressIndicator()
        }

        is ParadaUiState.Success -> {
            Scaffold(
                floatingActionButton = {
                    MyFAB {
                        abmParadaViewModel.onShowDialogClick();
                    }
                }
            ) { innerPadding ->
                MyRecyclerView(
                    modifier = Modifier.padding(innerPadding),
                    items= (uiState as ParadaUiState.Success).paradas,
                    onItemClick = {navigationController.navigate(Routes.ParadaDetail.createRoute(it.id))},
                    onItemLongPress = {abmParadaViewModel.onShowConfirmDeleteDialogClick(it)})
                {
                    Text(
                        text = it.toString(),
                        modifier = Modifier
                            .padding(16.dp),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                AddParadaDialog(
                    show = showDialog,
                    onDismiss = { abmParadaViewModel.onDialogClose() },
                    onParadaAdded = { nombre, direccion ->
                        abmParadaViewModel.onParadaCreated(nombre, direccion)
                    }
                )
                MyConfirmDeleteDialog(
                    show = showDeleteDialog,
                    itemToRemove = abmParadaViewModel.paradaSelected.toString(),
                    onDismiss = { abmParadaViewModel.onConfirmDialogClose() },
                    onLineaDeleted = { abmParadaViewModel.onItemRemove() })
                MyMessageDialog(show = showMessage,message = abmParadaViewModel.message, onDismiss = { abmParadaViewModel.onMessageDialogClose() }
                )
            }
        }
    }
}

@Composable
fun AddParadaDialog(show: Boolean, onDismiss: () -> Unit, onParadaAdded: (String, String) -> Unit){
    var nombre by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    if (show) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Añadir parada",
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
                        Text(text = "Nombre de la parada")
                    }
                )
                Spacer(modifier = Modifier.size(16.dp))
                TextField(
                    value = direccion,
                    onValueChange = { direccion = it },
                    singleLine = true,
                    maxLines = 1,
                    label = {
                        Text(text = "Dirección de la parada")
                    }
                )
                Spacer(modifier = Modifier.size(16.dp))
                Button(onClick = {
                    onParadaAdded(nombre,direccion)
                    nombre = ""
                    direccion = ""
                }, Modifier.fillMaxWidth()) {
                    Text(text = "Añadir")
                }
            }
        }
    }
}