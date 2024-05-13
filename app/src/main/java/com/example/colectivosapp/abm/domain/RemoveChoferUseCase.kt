package com.example.colectivosapp.abm.domain

import com.example.colectivosapp.abm.data.repositories.ChoferRepository
import com.example.colectivosapp.abm.ui.model.Chofer
import javax.inject.Inject

class RemoveChoferUseCase @Inject constructor(private val choferRepository: ChoferRepository) {
    suspend operator fun invoke(chofer: Chofer) {
        choferRepository.delete(chofer)
    }

}