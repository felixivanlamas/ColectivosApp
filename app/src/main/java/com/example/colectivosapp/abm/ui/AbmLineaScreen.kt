package com.example.colectivosapp.abm.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.example.colectivosapp.abm.ui.myComponents.MyRecyclerView
import com.example.colectivosapp.abm.ui.myComponents.MyShowError
import com.example.colectivosapp.abm.ui.state.LineaUiState

@Composable
fun AbmLineaScreen(abmLineaviewModel: AbmLineaViewModel, navigationController: NavHostController) {
    val showDialog: Boolean by abmLineaviewModel.showDialog.observeAsState(initial = false)
    val showDeleteDialog: Boolean by abmLineaviewModel.showConfirmDeleteDialog.observeAsState(initial = false)
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val uiState by produceState<LineaUiState>(
        initialValue = LineaUiState.Loading,
        key1 = lifecycle,
        key2 = abmLineaviewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            abmLineaviewModel.uiState.collect { value = it }
        }
    }

    when (uiState) {
        is LineaUiState.Error -> {
            MyShowError((uiState as LineaUiState.Error).throwable)
        }

        is LineaUiState.Loading -> {
            CircularProgressIndicator()
        }

        is LineaUiState.Success -> {
            Scaffold(
                floatingActionButton = {
                   MyFAB {
                       abmLineaviewModel.onShowDialogClick()
                   }
                }
            ) { innerPadding ->
                AddLineaDialog(
                    show = showDialog,
                    onDismiss = { abmLineaviewModel.onDialogClose() },
                    onLineaAdded = { abmLineaviewModel.onLineaCreated(it) })
                MyRecyclerView(
                    modifier = Modifier.padding(innerPadding),
                    items = ((uiState as LineaUiState.Success).lineas),
                    onItemClick = {navigationController.navigate(Routes.LineaDetail.createRoute(it.id))},
                    onItemLongPress = {abmLineaviewModel.onShowConfirmDeleteDialogClick(it)})
                {
                    Text(
                        text = it.toString(),
                        modifier = Modifier
                            .padding(16.dp),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                MyConfirmDeleteDialog(
                    show = showDeleteDialog,
                    itemToRemove = abmLineaviewModel.lineaSelected.toString(),
                    onDismiss = { abmLineaviewModel.onConfirmDialogClose() },
                    onDeleted = { abmLineaviewModel.onItemRemove() })
            }
        }
    }
}

@Composable
fun AddLineaDialog(show: Boolean, onDismiss: () -> Unit, onLineaAdded: (String) -> Unit) {
    var newLineaName by remember { mutableStateOf("") }
    if (show) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Añadir linea nueva",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.align(
                        Alignment.CenterHorizontally
                    )
                )
                Spacer(modifier = Modifier.size(16.dp))
                Row{
                    Text(text = "Linea: ", Modifier.align(Alignment.CenterVertically))
                    TextField(
                        value = newLineaName,
                        onValueChange = { newLineaName = it },
                        singleLine = true,
                        maxLines = 1,
                        label = {
                            Text(text = "Numero de la linea")
                        }
                    )
                }
                Spacer(modifier = Modifier.size(16.dp))
                Button(modifier = Modifier.align(Alignment.CenterHorizontally), onClick = {
                    onLineaAdded(newLineaName)
                    newLineaName = ""
                }) {
                    Text(text = "Añadir")
                }
            }
        }
    }
}