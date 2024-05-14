package com.example.colectivosapp.abm.domain

import com.example.colectivosapp.abm.data.repositories.ColectivoRepository
import com.example.colectivosapp.abm.data.repositories.LineaRepository
import com.example.colectivosapp.abm.ui.model.Colectivo
import javax.inject.Inject

class AddColectivoUseCase @Inject constructor(
    private val colectivoRepository: ColectivoRepository,
    private val lineaRepository: LineaRepository
) {
    suspend operator fun invoke(colectivo: Colectivo): Result<Unit> {
        val lineaId = colectivo.lineaId
        return try {
            if (colectivo.lineaId == 0) {
                Result.failure(IllegalArgumentException("No se puede agregar un colectivo sin lineaId"))
            }
            else if (lineaId?.let { lineaRepository.getLineaById(lineaId) } == null) {
                Result.failure(IllegalArgumentException("La línea de colectivo $lineaId no existe."))
            }
            else if (colectivo.patente.isBlank()) {
                Result.failure(IllegalArgumentException("La patente del colectivo no puede estar vacía."))
            } else {
                colectivoRepository.add(colectivo)
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}