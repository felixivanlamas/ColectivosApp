package com.example.colectivosapp.abm.ui.myComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun MyMessageDialog(show:Boolean,message: String, onDismiss: ()->Unit) {
    if (show){
        Dialog(
            onDismissRequest = { onDismiss() }
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)){
                Text(text = message)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { onDismiss() }, modifier = Modifier.align(Alignment.End)) {
                    Text(text = "Ok")
                }
            }
        }
    }
}