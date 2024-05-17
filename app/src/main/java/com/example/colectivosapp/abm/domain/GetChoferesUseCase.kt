package com.example.colectivosapp.abm.domain

import com.example.colectivosapp.abm.data.repositories.ChoferRepository
import com.example.colectivosapp.abm.ui.model.Chofer
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetChoferesUseCase @Inject constructor(private val choferRepository: ChoferRepository) {
    operator fun invoke() : Flow<List<Chofer>> = choferRepository.choferes
}