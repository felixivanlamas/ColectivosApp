package com.example.colectivosapp.abm.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.colectivosapp.abm.ui.model.RowDataHistorial
import com.example.colectivosapp.abm.ui.myComponents.MyShowError
import com.example.colectivosapp.abm.ui.state.HistorialUiState

@Composable
fun HistorialScreen(
    historialViewModel: HistorialViewModel) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val uiState by produceState<HistorialUiState>(
        initialValue = HistorialUiState.Loading,
        key1 = lifecycle,
        key2 = historialViewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            historialViewModel.uiStateHistorial.collect { value = it }
        }
    }

    when (uiState) {
        is HistorialUiState.Error -> {
            MyShowError((uiState as HistorialUiState.Error).throwable)
        }

        is HistorialUiState.Loading -> {
            CircularProgressIndicator()
        }

        is HistorialUiState.Success -> {
            Table((uiState as HistorialUiState.Success).rowDataHistorial)
        }
    }
}
@Composable
fun Table(data: List<RowDataHistorial>) {
    Column {
        TableRowHeader()
        data.forEach { rowData ->
            TableRow(rowData = rowData)
        }
    }
}

@Composable
fun TableRowHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(BorderStroke(1.dp, Color.Gray))
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MyCelda(text = "Pasajero", modifier = Modifier.weight(1f))
        MyCelda(text = "Sube ID", modifier = Modifier.weight(1f))
        MyCelda(text = "Parada de subida", modifier = Modifier.weight(1f))
        MyCelda(text = "Parada de bajada", modifier = Modifier.weight(1f))
        MyCelda(text = "Fecha", modifier = Modifier.weight(1f))
    }
}

@Composable
fun TableRow(rowData: RowDataHistorial) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(BorderStroke(1.dp, Color.Gray))
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MyCelda(text = rowData.pasajeroNombre, modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.width(1.dp))
        MyCelda(text = rowData.subeId, modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.width(1.dp))
        MyCelda(text = rowData.paradaSubida, modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.width(1.dp))
        MyCelda(text = rowData.paradaBajada, modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.width(1.dp))
        MyCelda(text = rowData.fecha, modifier = Modifier.weight(1f))
    }
}

@Composable
fun MyCelda(text: String?, modifier: Modifier) {
    Text(text = text ?: "", modifier = modifier,maxLines = 1)
}

//VERSION MEJORADA
//@Composable
//fun Table(
//    modifier: Modifier = Modifier,
//    rowModifier: Modifier = Modifier,
//    verticalLazyListState: LazyListState = rememberLazyListState(),
//    horizontalScrollState: ScrollState = rememberScrollState(),
//    columnCount: Int,
//    rowCount: Int,
//    beforeRow: (@Composable (rowIndex: Int) -> Unit)? = null,
//    afterRow: (@Composable (rowIndex: Int) -> Unit)? = null,
//    cellContent: @Composable (columnIndex: Int, rowIndex: Int) -> Unit
//) {
//    val columnWidths = remember { mutableStateMapOf<Int, Int>() }
//
//    Box(modifier = modifier.then(Modifier.horizontalScroll(horizontalScrollState))) {
//        LazyColumn(state = verticalLazyListState) {
//            items(rowCount) { rowIndex ->
//                Column {
//                    beforeRow?.invoke(rowIndex)
//
//                    Row(modifier = rowModifier) {
//                        (0 until columnCount).forEach { columnIndex ->
//                            Box(modifier = Modifier.layout { measurable, constraints ->
//                                val placeable = measurable.measure(constraints)
//
//                                val existingWidth = columnWidths[columnIndex] ?: 0
//                                val maxWidth = maxOf(existingWidth, placeable.width)
//
//                                if (maxWidth > existingWidth) {
//                                    columnWidths[columnIndex] = maxWidth
//                                }
//
//                                layout(width = maxWidth, height = placeable.height) {
//                                    placeable.placeRelative(0, 0)
//                                }
//                            }) {
//                                cellContent(columnIndex, rowIndex)
//                            }
//                        }
//                    }
//
//                    afterRow?.invoke(rowIndex)
//                }
//            }
//        }
//    }
//}