package com.example.colectivosapp.abm.domain

import com.example.colectivosapp.abm.data.repositories.RecorridoRepository
import com.example.colectivosapp.abm.ui.model.Recorrido
import javax.inject.Inject

class AddRecorridoUseCase @Inject constructor(private val recorridoRepository: RecorridoRepository) {
    suspend operator fun invoke(recorrido: Recorrido, paradasIds: List<Int>) {
        recorridoRepository.insertRecorrido(recorrido, paradasIds)
    }
}