package com.example.colectivosapp.abm.domain

import com.example.colectivosapp.abm.data.repositories.RecorridoRepository
import com.example.colectivosapp.abm.ui.model.Recorrido
import javax.inject.Inject

class RemoveRecorridoUseCase @Inject constructor(private val repository: RecorridoRepository) {
    suspend operator fun invoke(recorrido: Recorrido) = repository.deleteRecorrido(recorrido)
}