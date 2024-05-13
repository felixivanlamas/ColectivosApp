package com.example.colectivosapp.abm.ui.myComponents

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun <T> MyRecyclerView(
    items: List<T>,
    modifier: Modifier = Modifier,
    onItemClick: (T) -> Unit,
    onItemLongPress: (T) -> Unit,
    content: @Composable (item: T) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(items) { item ->
            MyItemCard(
                onItemSelected = { onItemClick(item) },
                onLongPress = { onItemLongPress(item) },
                content = {
                    content(item)
                }
            )
        }
    }
}
