package com.example.colectivosapp.abm.domain

import com.example.colectivosapp.abm.data.repositories.LineaRepository
import com.example.colectivosapp.abm.ui.model.Linea
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLineasUseCase @Inject constructor(private val lineaRepository: LineaRepository) {
    operator fun invoke(): Flow<List<Linea>> = lineaRepository.lineas
}