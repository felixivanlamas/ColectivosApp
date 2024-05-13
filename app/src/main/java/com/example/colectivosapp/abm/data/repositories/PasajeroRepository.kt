package com.example.colectivosapp.abm.data.repositories

import com.example.colectivosapp.abm.data.dao.PasajeroDao
import com.example.colectivosapp.abm.data.entities.PasajeroEntity
import com.example.colectivosapp.abm.ui.model.Pasajero
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PasajeroRepository @Inject constructor(
    private val pasajeroDao: PasajeroDao
){
    val pasajeros: Flow<List<Pasajero>> =
        pasajeroDao.getAllPasajeros().map { items -> items.map { it.toPasajero() } }

    suspend fun add(pasajero: Pasajero) {
        pasajeroDao.insertPasajero(pasajero.toEntity())
    }

    suspend fun getPasajeroById(pasajeroId: Int): Pasajero {
        return pasajeroDao.getPasajeroById(pasajeroId).toPasajero()
    }
}

fun Pasajero.toEntity() =
    PasajeroEntity(pasajeroId, nombre, apellido, dni, subeId)

fun PasajeroEntity.toPasajero()=
    Pasajero(pasajeroId,nombre,apellido,dni,subeId)