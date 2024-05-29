package com.example.colectivosapp.abm.ui.myComponents

import androidx.compose.runtime.Composable
import com.example.colectivosapp.abm.ui.model.ParadaOrden
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MyTrackerMapMarker(markItems:List<ParadaOrden>, itemActual: Int?){
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 18f)
    }
    GoogleMap (
        cameraPositionState = cameraPositionState
        ) {
        for (item in markItems){
            if(item.parada.latitud != 0.0 && item.parada.longitud != 0.0){
                if (item.orden == itemActual) {
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(LatLng(item.parada.latitud!!, item.parada.longitud!!), 18f)
                }
                Marker(
                    state = MarkerState(position = LatLng(item.parada.latitud!!, item.parada.longitud!!)),
                    title = item.parada.nombre,
                    snippet = item.parada.direccion,
                    icon = BitmapDescriptorFactory.defaultMarker(if (item.orden == itemActual) {BitmapDescriptorFactory.HUE_ORANGE} else {BitmapDescriptorFactory.HUE_GREEN})
                )
            }
        }
    }
}