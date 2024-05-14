package com.example.colectivosapp.abm.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.colectivosapp.abm.ui.model.ParadaOrden
import com.example.colectivosapp.abm.ui.model.Pasajero
import com.example.colectivosapp.abm.ui.model.Recorrido
import com.example.colectivosapp.abm.ui.model.Routes
import com.example.colectivosapp.abm.ui.myComponents.MyDynamicSelectTextField
import com.example.colectivosapp.abm.ui.myComponents.MyShowError
import com.example.colectivosapp.abm.ui.state.PasajeroUiState
import com.example.colectivosapp.abm.ui.state.RecorridoUiState
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SimulacionScreen(
    simulacionViewModel: SimulacionViewModel,
    navigationController: NavHostController
){
    LaunchedEffect(Unit) {
        simulacionViewModel.onShowSimulacionClick()
    }
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    val coroutineScope = rememberCoroutineScope()
    val show: Boolean by simulacionViewModel.showSimulacion.observeAsState(initial = true)
    val pasajeroSeleccionado by simulacionViewModel.pasajeroSeleccionado.observeAsState("")
    val recorridoSeleccionado by simulacionViewModel.recorridoSeleccionado.observeAsState("")
    val paradasRecorridoSeleccionado by simulacionViewModel.paradasRecorridoSeleccionado.observeAsState(initial = emptyList())
    val paradaActual by simulacionViewModel.paradaActual.observeAsState(0)
    val paradaSubida by simulacionViewModel.paradaSubida.observeAsState(null)
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val uiStatePasjeros by produceState<PasajeroUiState>(
        initialValue = PasajeroUiState.Loading,
        key1 = lifecycle,
        key2 = simulacionViewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            simulacionViewModel.uiStatePasajeros.collect { value = it }
        }
    }
    val uiStateRecorridos by produceState<RecorridoUiState>(
        initialValue = RecorridoUiState.Loading,
        key1 = lifecycle,
        key2 = simulacionViewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            simulacionViewModel.uiStateRecorridos.collect { value = it }
        }
    }

    when (uiStatePasjeros) {
        is PasajeroUiState.Error -> {
            MyShowError((uiStatePasjeros as PasajeroUiState.Error).throwable)
        }

        is PasajeroUiState.Loading -> {
            CircularProgressIndicator()
        }

        is PasajeroUiState.Success -> {
            Scaffold {
                Box(modifier = Modifier
                    .padding(it)
                    .fillMaxSize()){
                    SimulacionDialog(
                        modifier = Modifier.align(Alignment.Center),
                        show = show,
                        pasajeroSeleccionado = pasajeroSeleccionado.toString(),
                        recorridoSeleccionado = recorridoSeleccionado.toString(),
                        pasajeros = (uiStatePasjeros as PasajeroUiState.Success).pasajeros,
                        recorridos = (uiStateRecorridos as RecorridoUiState.Success).recorridos,
                        onPasajeroChange = { pasajero -> simulacionViewModel.onPasajeroChange(pasajero) },
                        onRecorridoChange = { recorrido -> simulacionViewModel.onRecorridoChange(recorrido) },
                        onBackClick = {
                            simulacionViewModel.onBackClick()
                            navigationController.popBackStack() },
                        iniciarSimulacionClick = {
                            simulacionViewModel.iniciarSimulacion()
                        })
                    if (paradasRecorridoSeleccionado != null) {
                        RecyclerViewSimulacion(
                            show = show,
                            items = paradasRecorridoSeleccionado!!,
                            paradaActual = paradaActual,
                            paradaSubida = paradaSubida ,
                            onClick = {
                                if (paradaSubida == null){
                                    simulacionViewModel.subirPasajero()
                                } else {
                                    simulacionViewModel.bajarPasajero()
                                    navigationController.navigate(Routes.HomeScreen.route)
                                    coroutineScope.launch {
                                        snackBarHostState.showSnackbar(
                                            message = "Viaje finalizado. Gracias por viajar con nosotros"
                                        )
                                    }
                                }
                            }) }
                    }
                }
            }
        }
    }

@Composable
fun SimulacionDialog(
    show:Boolean,
    pasajeroSeleccionado: String,
    recorridoSeleccionado: String,
    pasajeros: List<Pasajero>,
    recorridos: List<Recorrido>,
    onPasajeroChange: (Pasajero) -> Unit,
    onRecorridoChange: (Recorrido) -> Unit,
    iniciarSimulacionClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
    ){
    if (show){
        Dialog(onDismissRequest = {onBackClick()}){
            Column (modifier = modifier) {
                MyDynamicSelectTextField(
                    selectedValue = pasajeroSeleccionado,
                    options = pasajeros.map { pasajero ->  pasajero.toString() },
                    label = "Seleccione pasajero",
                    onValueChangedEvent = {
                        val selectedPasajero =
                            pasajeros.find { pasajero -> pasajero.toString() == it }
                        selectedPasajero?.let { pasajero ->
                            onPasajeroChange(pasajero)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                MyDynamicSelectTextField(
                    selectedValue = recorridoSeleccionado,
                    options = recorridos.map { recorrido ->  recorrido.toString() },
                    label = "Seleccione recorrido",
                    onValueChangedEvent = {
                        val selectedRecorrido =
                            recorridos.find { recorrido -> recorrido.toString() == it }
                        selectedRecorrido?.let { recorrido ->
                            onRecorridoChange(recorrido)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { iniciarSimulacionClick()},
                    Modifier.align(Alignment.CenterHorizontally)) {
                    Text(text = "Iniciar simulacion")
                }
            }
        }
    }
}
@Composable
fun RecyclerViewSimulacion(
    show: Boolean,
    items: List<ParadaOrden>,
    paradaActual: Int?,
    paradaSubida: Int?,
    onClick: () -> Unit
){
    if (!show) {
        Box(modifier = Modifier.fillMaxSize()){
            LazyColumn(modifier = Modifier.align(Alignment.Center)) {
                items(items) { item ->
                    MyItemCard(item = item, paradaActual)
                }
            }
            Button(
                onClick = { onClick() },
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 32.dp),
                colors = ButtonColors(
                    containerColor = if (paradaSubida == null) Color.Green else Color.Red,
                    contentColor = Color.White,
                    disabledContainerColor = Color.Gray,
                    disabledContentColor = Color.White
                )
            ){
                if (paradaSubida != null){
                    Text(text = "Bajar")
                } else
                    Text(text = "Subir")
            }
        }
    }
}

@Composable
fun MyItemCard(item: ParadaOrden, paradaActual:Int?) {
    ElevatedCard (
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        shape = RoundedCornerShape(16.dp),
        colors = CardColors(
            containerColor = if (paradaActual == item.orden) Color.Green else Color.White,
            contentColor = Color.Black,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.White),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()) {
        Text(
            text = item.parada.toString())
    }
}