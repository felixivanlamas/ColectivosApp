package com.example.colectivosapp.abm.domain

import com.example.colectivosapp.abm.data.repositories.ColectivoRepository
import com.example.colectivosapp.abm.data.repositories.LineaRepository
import com.example.colectivosapp.abm.ui.model.Colectivo
import com.example.colectivosapp.abm.ui.model.Linea
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetColectivosUseCase @Inject constructor(private val colectivoRepository: ColectivoRepository) {
    operator fun invoke(): Flow<List<Colectivo>> = colectivoRepository.colectivos
}