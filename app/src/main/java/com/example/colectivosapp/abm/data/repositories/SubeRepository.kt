package com.example.colectivosapp.abm.data.repositories

import com.example.colectivosapp.abm.data.dao.SubeDao
import com.example.colectivosapp.abm.data.entities.SubeEntity
import com.example.colectivosapp.abm.ui.model.Sube
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubeRepository @Inject constructor(private val subeDao: SubeDao){
    val subeList: Flow<List<Sube>> =
        subeDao.getAllSube().map { items -> items.map { it.toSube() } }

    suspend fun add(sube: Sube):Long{
        return subeDao.insertSube(sube.toEntity())
    }

    suspend fun delete(sube: Sube) {
        subeDao.deleteSube(sube.toEntity())
    }

    suspend fun getParadaById(subeId: Long): Sube {
        return subeDao.getSubeById(subeId).toSube()
    }
}

fun Sube.toEntity() = SubeEntity(subeId)

fun SubeEntity.toSube() = Sube(subeId)