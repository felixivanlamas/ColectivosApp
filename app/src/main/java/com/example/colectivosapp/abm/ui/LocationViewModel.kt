package com.example.colectivosapp.abm.ui

import android.annotation.SuppressLint
import android.app.Application
import android.location.Location
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(application: Application) : ViewModel() {
    private val _isMyLocationEnabled = MutableLiveData<Boolean>()
    val isMyLocationEnabled: LiveData<Boolean> get() = _isMyLocationEnabled
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)
    private val _location = MutableLiveData<Location?>()
    val location: LiveData<Location?> get() = _location

    private val locationRequest: LocationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY, 10000L
    ).apply {
        setMinUpdateIntervalMillis(5000L)
    }.build()

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.locations.lastOrNull()?.let {
                _location.postValue(it)
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    override fun onCleared() {
        super.onCleared()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    fun enableMyLocation() {
        _isMyLocationEnabled.value = true
    }
}