package com.example.colectivosapp.abm.domain

import com.example.colectivosapp.abm.data.repositories.RecorridoRepository
import com.example.colectivosapp.abm.ui.model.Recorrido
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecorridosUseCase @Inject constructor(private val recorridoRepository: RecorridoRepository) {
    operator fun invoke(): Flow<List<Recorrido>> = recorridoRepository.recorridos
}