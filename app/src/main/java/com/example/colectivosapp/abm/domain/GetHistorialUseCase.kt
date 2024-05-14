package com.example.colectivosapp.abm.domain

import com.example.colectivosapp.abm.data.repositories.HistorialRepository
import com.example.colectivosapp.abm.data.repositories.ParadaRepository
import com.example.colectivosapp.abm.data.repositories.PasajeroRepository
import com.example.colectivosapp.abm.ui.model.RowDataHistorial
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetHistorialUseCase @Inject constructor(
    private val historialRepository: HistorialRepository,
    private val pasajeroRepository: PasajeroRepository,
    private val paradaRepository: ParadaRepository
) {
    operator fun invoke(): Flow<List<RowDataHistorial>> {
        return historialRepository.historialCompleto.map { historiales ->
            historiales.map { historial ->
                val pasajeroNombre = pasajeroRepository.getPasajeroById(historial.pasajeroId).toString()
                val paradaSubidaNombre = paradaRepository.getParadaById(historial.paradaSubidaId).nombre
                val paradaBajadaNombre = paradaRepository.getParadaById(historial.paradaBajadaId).nombre
                RowDataHistorial(pasajeroNombre, historial.subeId.toString(), paradaSubidaNombre, paradaBajadaNombre, historial.fecha)
            }
        }
    }
}