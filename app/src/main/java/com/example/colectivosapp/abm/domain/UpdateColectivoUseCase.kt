package com.example.colectivosapp.abm.domain

import com.example.colectivosapp.abm.data.repositories.ChoferRepository
import com.example.colectivosapp.abm.data.repositories.ColectivoRepository
import com.example.colectivosapp.abm.ui.model.Colectivo
import javax.inject.Inject

class UpdateColectivoUseCase @Inject constructor(
    private val colectivoRepository: ColectivoRepository) {
    suspend operator fun invoke(colectivo: Colectivo) {
        colectivoRepository.update(colectivo)
    }
}