package com.example.colectivosapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.colectivosapp.abm.ui.model.Routes
import com.example.colectivosapp.abm.ui.myComponents.MyButtonFunction

@Composable
fun HomeScreen(navigationController: NavHostController) {
    Box (modifier = Modifier.fillMaxSize()){
        Text(text = "Bienvendo/a!",
            modifier = Modifier
                .padding(16.dp),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge)
        Column (modifier = Modifier.align(Alignment.Center)){
            MyButtonFunction(modifier = Modifier.fillMaxWidth().padding(32.dp), "Registrar usuario"){
                navigationController.navigate(Routes.RegistroPasajero.route)
            }
            MyButtonFunction(modifier = Modifier.fillMaxWidth().padding(32.dp),"Simular viaje"){
                navigationController.navigate(Routes.SimulacionScreen.route)
            }
        }
    }
}