package com.example.colectivosapp.abm.domain

import com.example.colectivosapp.abm.data.repositories.HistorialRepository
import com.example.colectivosapp.abm.ui.model.Historial
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHistorialUseCase @Inject constructor(private val historialRepository: HistorialRepository) {
    operator fun invoke(): Flow<List<Historial>> = historialRepository.historialCompleto
}