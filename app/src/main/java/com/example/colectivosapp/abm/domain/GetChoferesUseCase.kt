package com.example.colectivosapp.abm.domain

import com.example.colectivosapp.abm.data.repositories.ChoferRepository
import javax.inject.Inject

class GetChoferesUseCase @Inject constructor(private val choferRepository: ChoferRepository) {
    operator fun invoke() = choferRepository.choferes
}