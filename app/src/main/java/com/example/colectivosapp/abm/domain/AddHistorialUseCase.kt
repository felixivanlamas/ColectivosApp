package com.example.colectivosapp.abm.domain

import com.example.colectivosapp.abm.data.repositories.HistorialRepository
import com.example.colectivosapp.abm.ui.model.Historial
import javax.inject.Inject

class AddHistorialUseCase @Inject constructor(private val historialRepository: HistorialRepository) {
    suspend operator fun invoke(historial: Historial) = historialRepository.insertHistorial(historial)
}