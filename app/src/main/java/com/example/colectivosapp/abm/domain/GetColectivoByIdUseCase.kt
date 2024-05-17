package com.example.colectivosapp.abm.domain

import com.example.colectivosapp.abm.data.repositories.ChoferRepository
import com.example.colectivosapp.abm.data.repositories.ColectivoRepository
import com.example.colectivosapp.abm.data.repositories.LineaRepository
import com.example.colectivosapp.abm.data.repositories.RecorridoRepository
import com.example.colectivosapp.abm.ui.model.ColectivoCompleto
import javax.inject.Inject

class GetColectivoByIdUseCase @Inject constructor(
    private val colectivoRepository: ColectivoRepository,
    private val lineaRepository: LineaRepository,
    private val choferRepository: ChoferRepository,
    private val recorridoRepository: RecorridoRepository
) {
    suspend operator fun invoke(id: Int): ColectivoCompleto {
        val colectivo = colectivoRepository.getColectivoById(id)
        val linea = colectivo.lineaId?.let { lineaRepository.getLineaById(it) }
        val chofer = colectivo.choferId?.let { choferRepository.getChoferById(it) }
        val recorrido = colectivo.recorridoId?.let { recorridoRepository.getRecorridoById(it) }
        return ColectivoCompleto(colectivo.id, colectivo.patente, linea, chofer, recorrido)
    }
}