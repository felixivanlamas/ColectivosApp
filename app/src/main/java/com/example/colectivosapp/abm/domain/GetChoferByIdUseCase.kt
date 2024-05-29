package com.example.colectivosapp.abm.domain

import com.example.colectivosapp.abm.data.repositories.ChoferRepository
import javax.inject.Inject

class GetChoferByIdUseCase @Inject constructor(private val choferRepository: ChoferRepository){
    suspend operator fun invoke(id: Int) = choferRepository.getChoferById(id)
}