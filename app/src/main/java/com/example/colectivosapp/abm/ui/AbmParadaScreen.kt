package com.example.colectivosapp.abm.ui

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.colectivosapp.abm.ui.model.Routes
import com.example.colectivosapp.abm.ui.myComponents.MyCenterPointMapMarker
import com.example.colectivosapp.abm.ui.myComponents.MyConfirmDeleteDialog
import com.example.colectivosapp.abm.ui.myComponents.MyFAB
import com.example.colectivosapp.abm.ui.myComponents.MyMessageDialog
import com.example.colectivosapp.abm.ui.myComponents.MyRecyclerView
import com.example.colectivosapp.abm.ui.myComponents.MyShowError
import com.example.colectivosapp.abm.ui.state.ParadaUiState
import com.google.android.gms.maps.model.LatLng

@Composable
fun AbmParadaScreen(
    abmParadaViewModel: AbmParadaViewModel,
    locationViewModel: LocationViewModel,
    navigationController: NavHostController
) {

    val nombre: String by abmParadaViewModel.nombre.observeAsState(initial = "")
    val direccion: String by abmParadaViewModel.direccion.observeAsState(initial = "")
    val latitud: String by abmParadaViewModel.latitud.observeAsState(initial = "0.0")
    val longitud: String by abmParadaViewModel.longitud.observeAsState(initial = "0.0")
    val showDialog: Boolean by abmParadaViewModel.showDialog.observeAsState(initial = false)
    val showDeleteDialog: Boolean by abmParadaViewModel.showConfirmDeleteDialog.observeAsState(
        initial = false
    )
    val showMessage: Boolean by abmParadaViewModel.showMessage.observeAsState(initial = false)
    val showMapMarker: Boolean by abmParadaViewModel.showMapMarker.observeAsState(initial = false)

    val isMyLocationEnabled:Boolean by locationViewModel.isMyLocationEnabled.observeAsState(initial = false)
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState<ParadaUiState>(
        initialValue = ParadaUiState.Loading,
        key1 = lifecycle,
        key2 = abmParadaViewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            abmParadaViewModel.uiState.collect { value = it }
        }
    }

    when (uiState) {
        is ParadaUiState.Error -> {
            MyShowError((uiState as ParadaUiState.Error).throwable)
        }

        is ParadaUiState.Loading -> {
            CircularProgressIndicator()
        }

        is ParadaUiState.Success -> {
            Scaffold(
                floatingActionButton = {
                    if (!showMapMarker) {
                        MyFAB {
                            abmParadaViewModel.onShowDialogClick();
                        }
                    }
                }
            ) { innerPadding ->
                MyRecyclerView(
                    modifier = Modifier.padding(innerPadding),
                    items = (uiState as ParadaUiState.Success).paradas,
                    onItemClick = { navigationController.navigate(Routes.ParadaDetail.createRoute(it.id)) },
                    onItemLongPress = { abmParadaViewModel.onShowConfirmDeleteDialogClick(it) })
                {
                    Text(
                        text = it.toString(),
                        modifier = Modifier
                            .padding(16.dp),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                AddParadaDialog(
                    nombre = nombre,
                    direccion = direccion,
                    latitud = latitud,
                    longitud = longitud,
                    show = showDialog,
                    onDismiss = { abmParadaViewModel.onDialogClose() },
                    onUbicacionClick = { abmParadaViewModel.onUbicacionClick() },
                    onParadaAdded = { abmParadaViewModel.onParadaCreated() },
                    onNombreChange = { abmParadaViewModel.onNombreChange(it) },
                    onDireccionChange = { abmParadaViewModel.onDireccionChange(it) },
                    onLatitudChange = { abmParadaViewModel.onLatitudChange(it) },
                    onLongitudChange = { abmParadaViewModel.onLongitudChange(it) }
                )
                MyConfirmDeleteDialog(
                    show = showDeleteDialog,
                    itemToRemove = abmParadaViewModel.paradaSelected.toString(),
                    onDismiss = { abmParadaViewModel.onConfirmDialogClose() },
                    onDeleted = { abmParadaViewModel.onItemRemove() })
                MyMessageDialog(
                    show = showMessage,
                    message = abmParadaViewModel.message,
                    onDismiss = { abmParadaViewModel.onMessageDialogClose() }
                )
                if (showMapMarker) {
                    RequestLocationPermission {
                        locationViewModel.startLocationUpdates()
                        locationViewModel.enableMyLocation()
                    }
                    MyCenterPointMapMarker(
                        isMyLocationEnabled = isMyLocationEnabled,
                        initialPosition = LatLng(latitud.toDouble(), longitud.toDouble()),
                        onClickListener = { abmParadaViewModel.onMapMarkerClick(it) },
                        onBack = { abmParadaViewModel.onMapMarkerBack() },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun AddParadaDialog(
    nombre: String,
    direccion: String,
    latitud: String,
    longitud: String,
    show: Boolean,
    onDismiss: () -> Unit,
    onUbicacionClick: () -> Unit,
    onParadaAdded: () -> Unit,
    onNombreChange: (String) -> Unit,
    onDireccionChange: (String) -> Unit,
    onLatitudChange: (String) -> Unit,
    onLongitudChange: (String) -> Unit
) {
    if (show) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Row {
                    Text(
                        text = "Añadir parada",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .weight(1f)
                    )
                    IconButton(
                        onClick = { onUbicacionClick() },
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "ubicacion"
                        )
                    }
                }
                Spacer(modifier = Modifier.size(16.dp))
                TextField(
                    value = nombre,
                    onValueChange = { onNombreChange(it) },
                    singleLine = true,
                    maxLines = 1,
                    label = { Text(text = "Nombre de la parada") }
                )
                Spacer(modifier = Modifier.size(16.dp))
                TextField(
                    value = direccion,
                    onValueChange = { onDireccionChange(it) },
                    singleLine = true,
                    maxLines = 1,
                    label = { Text(text = "Dirección de la parada") }
                )
                Spacer(modifier = Modifier.size(16.dp))
                TextField(
                    value = latitud,
                    onValueChange = { onLatitudChange(it) },
                    singleLine = true,
                    maxLines = 1,
                    label = { Text(text = "Latitud") }
                )
                Spacer(modifier = Modifier.size(16.dp))
                TextField(
                    value = longitud,
                    onValueChange = { onLongitudChange(it) },
                    singleLine = true,
                    maxLines = 1,
                    label = { Text(text = "Longitud") }
                )
                Spacer(modifier = Modifier.size(16.dp))
                Button(onClick = {
                    onParadaAdded()
                }, Modifier.fillMaxWidth()) {
                    Text(text = "Añadir")
                }
            }
        }
    }
}

@Composable
fun RequestLocationPermission(
    onPermissionGranted: () -> Unit
) {
    var permissionGranted by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        permissionGranted = isGranted
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    if (permissionGranted) {
        onPermissionGranted()
    }
}