package com.example.colectivosapp.abm.domain

import com.example.colectivosapp.abm.data.repositories.LineaRepository
import com.example.colectivosapp.abm.ui.model.Linea
import javax.inject.Inject

class RemoveLineaUseCase @Inject constructor(private val lineaRepository: LineaRepository) {
    suspend operator fun invoke(linea: Linea){
        lineaRepository.delete(linea)
    }
}