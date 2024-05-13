package com.example.colectivosapp.abm.domain

import com.example.colectivosapp.abm.data.repositories.ParadaRepository
import com.example.colectivosapp.abm.ui.model.Parada
import javax.inject.Inject

class RemoveParadaUseCase @Inject constructor(private val paradasRepository: ParadaRepository) {
    suspend operator fun invoke(parada: Parada) = paradasRepository.delete(parada)
}