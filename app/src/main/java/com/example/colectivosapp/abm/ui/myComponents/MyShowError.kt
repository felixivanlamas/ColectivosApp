package com.example.colectivosapp.abm.ui.myComponents

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun MyShowError(error: Throwable) {
    Text(text = error.message.toString(), color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
}