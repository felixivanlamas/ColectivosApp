package com.example.colectivosapp.abm.domain

import com.example.colectivosapp.abm.data.repositories.ColectivoRepository
import com.example.colectivosapp.abm.ui.model.Colectivo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetColectivosByLineaIdUseCase @Inject constructor(private val colectivoRepository: ColectivoRepository) {
    operator fun invoke(lineaId: Int): Flow<List<Colectivo>> = colectivoRepository.getColectivosByLineaId(lineaId)
}