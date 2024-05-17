package com.example.colectivosapp.abm.domain

import com.example.colectivosapp.abm.data.repositories.ParadaRepository
import com.example.colectivosapp.abm.ui.model.Parada
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetParadasUseCase @Inject constructor(private val paradaRepository: ParadaRepository) {
    operator fun invoke() : Flow<List<Parada>> = paradaRepository.paradas
}