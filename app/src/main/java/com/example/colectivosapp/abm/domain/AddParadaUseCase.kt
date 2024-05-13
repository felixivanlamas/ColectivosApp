package com.example.colectivosapp.abm.domain

import com.example.colectivosapp.abm.data.repositories.ParadaRepository
import com.example.colectivosapp.abm.ui.model.Parada
import javax.inject.Inject

class AddParadaUseCase @Inject constructor(private val paradaRepository: ParadaRepository) {
    suspend operator fun invoke(parada: Parada) = paradaRepository.add(parada)
}