package com.example.colectivosapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.colectivosapp.abm.ui.model.Routes
import com.example.colectivosapp.abm.ui.myComponents.MyButtonFunction

@Composable
fun HomeScreen(navigationController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(
                onClick = { navigationController.navigate(Routes.RegistroPasajero.route)},
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.registericon),
                    contentDescription = "Agregar"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Registrar usuario",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        Column(
            modifier = Modifier.align(Alignment.Center)
        ) {
            MyButtonFunction(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                text = "Simular viaje"
            ) {
                navigationController.navigate(Routes.SimulacionScreen.route)
            }
        }
    }
}

