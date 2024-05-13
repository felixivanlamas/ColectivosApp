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
import com.example.colectivosapp.abm.ui.state.ColectivoUiState

@Composable
fun AbmColectivoScreen(
    abmColectivoViewModel: AbmColectivoViewModel,
    navigationController: NavHostController
) {
    val showDialog: Boolean by abmColectivoViewModel.showDialog.observeAsState(initial = false)
    val showDeleteDialog: Boolean by abmColectivoViewModel.showConfirmDeleteDialog.observeAsState(initial = false)
    val showMessage: Boolean by abmColectivoViewModel.showMessage.observeAsState(initial = false)
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val uiState by produceState<ColectivoUiState>(
        initialValue = ColectivoUiState.Loading,
        key1 = lifecycle,
        key2 = abmColectivoViewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            abmColectivoViewModel.uiState.collect { value = it }
        }
    }

    when (uiState) {
        is ColectivoUiState.Error -> {
            MyShowError((uiState as ColectivoUiState.Error).throwable)
        }

        is ColectivoUiState.Loading -> {
            CircularProgressIndicator()
        }

        is ColectivoUiState.Success -> {
            Scaffold(
                floatingActionButton = {
                    MyFAB {
                        abmColectivoViewModel.onShowDialogClick();
                    }
                }
            ) { innerPadding ->
                MyRecyclerView(
                    modifier = Modifier.padding(innerPadding),
                    items= (uiState as ColectivoUiState.Success).colectivos,
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
                    onDismiss = { abmColectivoViewModel.onDialogClose() },
                    onColectivoAdded = { patente, linea ->
                        abmColectivoViewModel.onColectivoCreated(patente, linea)
                    }
                )
                MyConfirmDeleteDialog(
                    show = showDeleteDialog,
                    itemToRemove = abmColectivoViewModel.colectivoSelected.toString(),
                    onDismiss = { abmColectivoViewModel.onConfirmDialogClose() },
                    onLineaDeleted = { abmColectivoViewModel.onItemRemove() })
                MyMessageDialog(show = showMessage,message = abmColectivoViewModel.message, onDismiss = { abmColectivoViewModel.onMessageDialogClose() }
                )
            }
        }
    }
}

@Composable
fun AddColectivoDialog(show: Boolean, lineaId:Int? = null,onDismiss: () -> Unit, onColectivoAdded: (String,Int) -> Unit) {
    var patente by remember { mutableStateOf("") }
    var linea by remember { mutableStateOf(lineaId?.toString() ?: "") }
    if (show) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Column(
                Modifier
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
                Spacer(modifier = Modifier.size(16.dp))
                TextField(
                    value = patente,
                    onValueChange = { patente = it },
                    singleLine = true,
                    maxLines = 1,
                    label = {
                        Text(text = "Patente del colectivo")
                    }
                )
                Spacer(modifier = Modifier.size(16.dp))
                TextField(
                    enabled = lineaId == null,
                    value = linea,
                    onValueChange = { linea = it },
                    singleLine = true,
                    maxLines = 1,
                    label = {
                        Text(text = "Numero de linea")
                    }
                )
                Spacer(modifier = Modifier.size(16.dp))
                Button(onClick = {
                    if(linea != ""){
                        onColectivoAdded(patente,linea.toInt())
                    }
                    onColectivoAdded(patente,0)
                    patente = ""
                    linea = ""
                }, Modifier.fillMaxWidth()) {
                    Text(text = "Añadir")
                }
            }
        }
    }
}