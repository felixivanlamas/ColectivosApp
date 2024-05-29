package com.example.colectivosapp.abm.domain

import com.example.colectivosapp.abm.data.repositories.ChoferRepository
import com.example.colectivosapp.abm.data.repositories.ColectivoRepository
import com.example.colectivosapp.abm.ui.model.Chofer
import com.example.colectivosapp.abm.ui.model.Colectivo
import javax.inject.Inject

class UpdateChoferUseCase @Inject constructor(private val choferRepository: ChoferRepository, private val colectivoRepository: ColectivoRepository) {
    suspend operator fun invoke(chofer: Chofer) {
            choferRepository.insert(chofer)
        }
}