package com.example.colectivosapp.abm.domain

import com.example.colectivosapp.abm.data.repositories.RecorridoRepository
import javax.inject.Inject

class GetRecorridoByIdUseCase @Inject constructor(private val recorridoRepository: RecorridoRepository) {
    suspend operator fun invoke(recorridoId: Int) = recorridoRepository.getRecorridoWithParadas(recorridoId)
}