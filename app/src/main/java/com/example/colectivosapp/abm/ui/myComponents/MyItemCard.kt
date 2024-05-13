package com.example.colectivosapp.abm.ui.myComponents

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

@Composable
fun MyItemCard(
    onItemSelected: () -> Unit,
    onLongPress: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .pointerInput(Unit) {
                detectTapGestures(onLongPress = {
                    onLongPress()
                }, onTap = {
                    onItemSelected()
                })
            }
    ) {
        Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            content()
        }
    }
}

