package com.example.colectivosapp.abm.domain

import com.example.colectivosapp.abm.data.repositories.PasajeroRepository
import com.example.colectivosapp.abm.data.repositories.SubeRepository
import com.example.colectivosapp.abm.ui.model.Pasajero
import com.example.colectivosapp.abm.ui.model.Sube
import javax.inject.Inject

class AddPasajeroUseCase @Inject constructor(private val pasajeroRepository: PasajeroRepository, private val subeRepository: SubeRepository) {
    suspend operator fun invoke(pasajero: Pasajero) {
        try {
           val subeId = subeRepository.add(Sube())
            pasajero.subeId = subeId
            pasajeroRepository.add(pasajero)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}