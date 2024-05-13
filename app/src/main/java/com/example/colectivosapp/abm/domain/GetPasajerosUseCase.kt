package com.example.colectivosapp.abm.domain

import com.example.colectivosapp.abm.data.repositories.PasajeroRepository
import javax.inject.Inject

class GetPasajerosUseCase @Inject constructor(private val pasajeroRepository: PasajeroRepository) {
    operator fun invoke() = pasajeroRepository.pasajeros
}