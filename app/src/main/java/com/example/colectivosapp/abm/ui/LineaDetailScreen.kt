package com.example.colectivosapp.abm.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.colectivosapp.abm.ui.model.Colectivo
import com.example.colectivosapp.abm.ui.model.Routes
import com.example.colectivosapp.abm.ui.myComponents.MyConfirmDeleteDialog
import com.example.colectivosapp.abm.ui.myComponents.MyFAB
import com.example.colectivosapp.abm.ui.myComponents.MyRecyclerView
import com.example.colectivosapp.abm.ui.myComponents.MyShowError
import com.example.colectivosapp.abm.ui.state.ColectivoUiState


@Composable
fun LineaDetailScreen(
    lineaId: Int,
    lineaDetailViewModel: LineaDetailViewModel,
    navigationController: NavHostController
) {
    LaunchedEffect(key1 = lineaId) {
        lineaDetailViewModel.init(lineaId)
    }

    val showDialog: Boolean by lineaDetailViewModel.showDialog.observeAsState(initial = false)
    val showDeleteDialog: Boolean by lineaDetailViewModel.showConfirmDeleteDialog.observeAsState(initial = false)


    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val uiState by produceState<ColectivoUiState>(
        initialValue = ColectivoUiState.Loading,
        key1 = lifecycle,
        key2 = lineaDetailViewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            lineaDetailViewModel.uiState.collect { value = it }
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
                    MyFAB{
                        lineaDetailViewModel.onShowDialogClick()
                    }
                }
            ) { innerPadding ->
                Column(modifier = Modifier.padding(innerPadding)) {
                    Text(text = "Colectivos de la linea ${lineaDetailViewModel.lineaId.value}",
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp), color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleLarge)
                    MyRecyclerView(
                        modifier = Modifier.padding(innerPadding),
                        items= (uiState as ColectivoUiState.Success).colectivos,
                        onItemClick = {navigationController.navigate(Routes.ColectivoDetail.createRoute(it.id))},
                        onItemLongPress = {lineaDetailViewModel.onShowConfirmDeleteDialogClick(it)})
                    {
                        Text(
                            text = it.toString(),
                            modifier = Modifier
                                .padding(16.dp),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                //Dialogo para agregar un nuevo colectivo

                MyConfirmDeleteDialog(show = showDeleteDialog, itemToRemove = lineaDetailViewModel.colectivoSelected.toString(), onDismiss = { lineaDetailViewModel.onConfirmDialogClose() }) {
                    lineaDetailViewModel.removeColectivo()
                }
            }
        }
    }
}