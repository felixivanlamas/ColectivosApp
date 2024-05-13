package com.example.colectivosapp.abm.domain

import com.example.colectivosapp.abm.data.repositories.ParadaRepository
import javax.inject.Inject

class GetParadasUseCase @Inject constructor(private val paradaRepository: ParadaRepository) {
    operator fun invoke() = paradaRepository.paradas
}