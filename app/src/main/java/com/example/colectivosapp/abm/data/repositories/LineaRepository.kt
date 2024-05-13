package com.example.colectivosapp.abm.data.repositories

import com.example.colectivosapp.abm.data.dao.LineaDao
import com.example.colectivosapp.abm.data.entities.LineaEntity
import com.example.colectivosapp.abm.ui.model.Linea
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LineaRepository @Inject constructor(
    private val lineaDao: LineaDao
) {
    val lineas: Flow<List<Linea>> =
        lineaDao.getAllLineas().map { items -> items.map { Linea(it.id, it.nombre) } }

    suspend fun add(linea: Linea) {
        lineaDao.insertLinea(linea.toEntity())
    }

    suspend fun update(linea: Linea) {
        lineaDao.insertLinea(linea.toEntity())
    }

    suspend fun delete(linea: Linea) {
        lineaDao.deleteLinea(linea.toEntity())
    }

    suspend fun getLineaById(lineaId: Int): Linea {
        return lineaDao.getLineaById(lineaId).toLinea()
    }
}

fun Linea.toEntity() = LineaEntity(id, nombre)

fun LineaEntity.toLinea() = Linea(id, nombre)