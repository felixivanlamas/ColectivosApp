package com.example.colectivosapp.abm.ui.myComponents

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun MyButtonFunction (modifier: Modifier, text: String, onClick: () -> Unit ) {
    Button(onClick = { onClick() }, modifier = modifier) {
        Text(text = text, textAlign = TextAlign.Center)
    }
}