package com.example.colectivosapp.abm.ui.myComponents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun <T> MyFilterableDropdownMenu(
    items: List<T>,
    onItemSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    itemContent: @Composable (T) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Search") },
            modifier = Modifier.fillMaxWidth()
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            val filteredItems = if (searchText.isNotEmpty()) {
                items.filter {
                    it.toString().contains(searchText, ignoreCase = true)
                }
            } else {
                items
            }

            filteredItems.forEach { item ->
                DropdownMenuItem(text = { itemContent(item) },onClick = {
                    onItemSelected(item)
                    expanded = false
                })
            }
        }
    }
}
