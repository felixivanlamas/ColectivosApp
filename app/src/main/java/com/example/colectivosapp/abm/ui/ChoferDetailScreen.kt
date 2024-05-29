package com.example.colectivosapp.abm.ui

import android.util.Log
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
import com.example.colectivosapp.abm.ui.model.Colectivo
import com.example.colectivosapp.abm.ui.model.ColectivoCompleto
import com.example.colectivosapp.abm.ui.model.Recorrido
import com.example.colectivosapp.abm.ui.myComponents.MyDynamicSelectTextField
import com.example.colectivosapp.abm.ui.myComponents.MyShowError
import com.example.colectivosapp.abm.ui.state.ChoferUiState
import com.example.colectivosapp.abm.ui.state.ColectivoUiState
import com.example.colectivosapp.abm.ui.state.RecorridoUiState

@Composable
fun ChoferDetailScreen(
    choferId: Int,
    chofereScreenViewModel: ChoferDetailViewModel,
    navigationController: NavHostController
) {
    LaunchedEffect(key1 = choferId) {
        chofereScreenViewModel.getChofer(choferId)
    }
    val selectedChofer: Chofer? by chofereScreenViewModel.selectedChofer.observeAsState(initial = Chofer())
    val selectedColectivo: Colectivo? by chofereScreenViewModel.selectedColectivo.observeAsState(initial = Colectivo())
    val buttonState: Boolean by chofereScreenViewModel.buttonState.observeAsState(initial = false)


    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val uiStateColectivo by produceState<ColectivoUiState>(
        initialValue = ColectivoUiState.Loading,
        key1 = lifecycle,
        key2 = chofereScreenViewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            chofereScreenViewModel.uiStateColectivo.collect { value = it }
        }
    }

    when (uiStateColectivo) {
        is ColectivoUiState.Error -> {
            MyShowError((uiStateColectivo as ColectivoUiState.Error).throwable)
        }

        is ColectivoUiState.Loading -> {
            CircularProgressIndicator()
        }

        is ColectivoUiState.Success -> {
            Scaffold { it ->
                Box(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize()){

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                            Text(text = selectedChofer.toString(), modifier = Modifier.padding(16.dp))
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                        MyDynamicSelectTextField<Colectivo>(
                            selectedValue = selectedColectivo.toString(),
                            options = (uiStateColectivo as ColectivoUiState.Success).colectivos,
                            label = "Colectivo asignado",
                            onValueChangedEvent = {chofereScreenViewModel.selectColectivo(it)}
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                chofereScreenViewModel.updateChofer()
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