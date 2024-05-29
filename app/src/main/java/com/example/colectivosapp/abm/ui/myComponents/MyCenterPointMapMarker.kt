package com.example.colectivosapp.abm.ui.myComponents

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.colectivosapp.R
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MyCenterPointMapMarker(
    isMyLocationEnabled: Boolean,
    initialPosition: LatLng,
    onClickListener: (LatLng) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialPosition, 18f)
    }
    val mapProperties by remember { mutableStateOf(MapProperties(isMyLocationEnabled = isMyLocationEnabled)) }
    val uiSettings by remember { mutableStateOf( MapUiSettings(myLocationButtonEnabled = isMyLocationEnabled)) }

    BackHandler(onBack = { onBack() })
    Box(modifier = modifier) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            uiSettings = uiSettings
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            IconButton(
                onClick = {
                    onClickListener(
                        LatLng(
                            cameraPositionState.position.target.latitude,
                            cameraPositionState.position.target.longitude
                        )
                    )
                },
            ) {
                Image(
                    painter = painterResource(id = R.drawable.paradanaviconfilled),
                    contentDescription = "marker",
                )
            }
        }
    }
}