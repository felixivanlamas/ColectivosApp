package com.example.colectivosapp.abm.data.repositories

import com.example.colectivosapp.abm.data.dao.ParadaDao
import com.example.colectivosapp.abm.data.entities.ParadaEntity
import com.example.colectivosapp.abm.ui.model.Parada
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ParadaRepository @Inject constructor(
    private val paradaDao: ParadaDao
) {
    val paradas: Flow<List<Parada>> =
        paradaDao.getAllParadas().map { items -> items.map { it.toParada() } }

    suspend fun add(parada: Parada) {
        paradaDao.insertParada(parada.toEntity())
    }

    suspend fun delete(parada: Parada) {
        paradaDao.deleteParada(parada.toEntity())
    }

    suspend fun getParadaById(paradaId: Int): Parada {
        return paradaDao.getparadaById(paradaId).toParada()
    }
}

fun Parada.toEntity() = ParadaEntity(id, nombre, direccion)

fun ParadaEntity.toParada() = Parada(paradaId, nombreParada, direccion)