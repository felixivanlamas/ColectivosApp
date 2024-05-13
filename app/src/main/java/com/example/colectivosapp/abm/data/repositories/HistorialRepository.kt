package com.example.colectivosapp.abm.data.repositories

import com.example.colectivosapp.abm.data.dao.HistorialDao
import com.example.colectivosapp.abm.data.entities.HistorialEntity
import com.example.colectivosapp.abm.ui.model.Historial
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistorialRepository @Inject constructor(private val historialDao: HistorialDao){
    val historialCompleto: Flow<List<Historial>> = historialDao.getAllHistorial().map { item -> item.map { it.toHistorial() } }

    suspend fun insertHistorial(historial: Historial){
        historialDao.insertHistorial(historial.toHistoriaEntity())
    }
}

fun Historial.toHistoriaEntity() = HistorialEntity(pasajeroId = pasajeroId, subeId = subeId, paradaSubidaId = paradaSubidaId, paradaBajadaId = paradaBajadaId, fecha = fecha)
fun HistorialEntity.toHistorial() = Historial(id, pasajeroId,subeId,paradaSubidaId,paradaBajadaId,fecha)