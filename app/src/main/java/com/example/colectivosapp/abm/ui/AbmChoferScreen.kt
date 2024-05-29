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
import com.example.colectivosapp.abm.ui.state.ChoferUiState

@Composable
fun AbmChoferScreen(
    abmChoferScreenViewModel: AbmChoferScreenViewModel,
    navigationController: NavHostController
) {
    val showDialog: Boolean by abmChoferScreenViewModel.showDialog.observeAsState(initial = false)
    val showDeleteDialog: Boolean by abmChoferScreenViewModel.showConfirmDeleteDialog.observeAsState(initial = false)
    val showMessage: Boolean by abmChoferScreenViewModel.showMessage.observeAsState(initial = false)
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val uiState by produceState<ChoferUiState>(
        initialValue = ChoferUiState.Loading,
        key1 = lifecycle,
        key2 = abmChoferScreenViewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            abmChoferScreenViewModel.uiState.collect { value = it }
        }
    }

    when (uiState) {
        is ChoferUiState.Error -> {
            MyShowError((uiState as ChoferUiState.Error).throwable)
        }

        is ChoferUiState.Loading -> {
            CircularProgressIndicator()
        }

        is ChoferUiState.Success -> {
            Scaffold(
                floatingActionButton = {
                    MyFAB {
                        abmChoferScreenViewModel.onShowDialogClick();
                    }
                }
            ) { innerPadding ->
                MyRecyclerView(
                    modifier = Modifier.padding(innerPadding),
                    items = (uiState as ChoferUiState.Success).choferes,
                    onItemClick = { },
//                    onItemClick = {item -> navigationController.navigate(Routes.ChoferDetail.createRoute(item.id))} ,
                    onItemLongPress = {item -> abmChoferScreenViewModel.onShowConfirmDeleteDialogClick(item)}
                ) {
                    Text(
                        text = it.toString(),
                        modifier = Modifier.padding(16.dp),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                AddChoferDialog(
                    show = showDialog,
                    onDismiss = { abmChoferScreenViewModel.onDialogClose() },
                    onChoferAdded = { nombre, apellido, documento ->
                        abmChoferScreenViewModel.onChoferCreated(nombre, apellido, documento)
                    }
                )
                MyConfirmDeleteDialog(
                    show = showDeleteDialog,
                    itemToRemove = abmChoferScreenViewModel.choferSelected.toString(),
                    onDismiss = { abmChoferScreenViewModel.onConfirmDialogClose() },
                    onDeleted = { abmChoferScreenViewModel.onItemRemove() })
                MyMessageDialog(show = showMessage,message = abmChoferScreenViewModel.message, onDismiss = { abmChoferScreenViewModel.onMessageDialogClose() }
                )
            }
        }
    }
}

@Composable
fun AddChoferDialog(show: Boolean, onDismiss: () -> Unit, onChoferAdded: (String,String,String) -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var documento by remember { mutableStateOf("") }
    if (show) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Añadir chofer",
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
                        Text(text = "Nombre del chofer")
                    }
                )
                Spacer(modifier = Modifier.size(16.dp))
                TextField(
                    value = apellido,
                    onValueChange = { apellido = it },
                    singleLine = true,
                    maxLines = 1,
                    label = {
                        Text(text = "Apellido del chofer")
                    }
                )
                Spacer(modifier = Modifier.size(16.dp))
                TextField(
                    value = documento,
                    onValueChange = { documento = it },
                    singleLine = true,
                    maxLines = 1,
                    label = {
                        Text(text = "Documento del chofer")
                    }
                )
                Spacer(modifier = Modifier.size(16.dp))
                Button(onClick = {
                    onChoferAdded(nombre,apellido,documento)
                    nombre = ""
                    apellido = ""
                    documento = ""
                }, Modifier.fillMaxWidth()) {
                    Text(text = "Añadir")
                }
            }
        }
    }
}