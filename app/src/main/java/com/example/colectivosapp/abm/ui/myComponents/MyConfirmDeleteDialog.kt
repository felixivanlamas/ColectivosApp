package com.example.colectivosapp.abm.ui.myComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun MyConfirmDeleteDialog(show: Boolean, itemToRemove:String, onDismiss: () -> Unit, onLineaDeleted: () -> Unit) {
    if (show) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Column(verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .background(Color.White)
                    .padding(16.dp)) {
                Text(text = "Desea eliminar $itemToRemove?")
                Spacer(modifier = Modifier.size(16.dp))
                Button(onClick = { onLineaDeleted() }, modifier = Modifier.align(Alignment.End)) {
                    Text(text = "Confirmar")
                }
            }
        }
    }
}