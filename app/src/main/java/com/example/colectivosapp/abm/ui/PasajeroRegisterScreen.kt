package com.example.colectivosapp.abm.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.colectivosapp.abm.ui.model.Routes

@Composable
fun PasajeroRegister(
    pasajeroRegisterViewModel: PasajeroRegisterViewModel,
    navigationController: NavHostController
) {
    Box(modifier = Modifier.fillMaxSize()){
        Column(modifier = Modifier
            .align(Alignment.Center)){
            Text(text = "Registrar Pasajero",
                style = MaterialTheme.typography.headlineLarge ,
                modifier = Modifier.align(
                Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.size(16.dp))
            OutlinedTextField(
                value = pasajeroRegisterViewModel.nombrePasajero,
                maxLines = 1,
                onValueChange = {pasajeroRegisterViewModel.updateNombrePasajero(it)},
                label = { Text(text = "Nombre") })
            Spacer(modifier = Modifier.size(16.dp))
            OutlinedTextField(
                value = pasajeroRegisterViewModel.apellidoPasajero,
                maxLines = 1,
                onValueChange = {pasajeroRegisterViewModel.updateApellidoPasajero(it)},
                label = { Text(text = "Apellido") })
            Spacer(modifier = Modifier.size(16.dp))
            OutlinedTextField(
                value = pasajeroRegisterViewModel.dniPasajero,
                maxLines = 1,
                onValueChange = {pasajeroRegisterViewModel.updateDniPasajero(it)},
                label = { Text(text = "Dni") })
            Spacer(modifier = Modifier.size(16.dp))
            Button(onClick = {
                pasajeroRegisterViewModel.addPasajero()
                navigationController.navigate(Routes.HomeScreen.route)},
                modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text(text = "Registrar")
            }
        }
    }
}