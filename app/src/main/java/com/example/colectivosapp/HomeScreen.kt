package com.example.colectivosapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.colectivosapp.abm.ui.model.Routes

@Composable
fun HomeScreen(navigationController: NavHostController) {
    Box (modifier = Modifier.fillMaxSize()){
        Text(text = "Bienvendo/a!",
            modifier = Modifier
                .padding(16.dp),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge)
        Column (modifier = Modifier.align(Alignment.Center)){
            Button(onClick = { navigationController.navigate(Routes.RegistroPasajero.route)}
            ) {
                Text(text = "Registrar usuario")
            }
            Button(onClick = { navigationController.navigate(Routes.SimulacionScreen.route)}
            ) {
                Text(text = "SIMULACION")
            }
        }
    }
}