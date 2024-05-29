package com.example.colectivosapp.abm.data.repositories

import com.example.colectivosapp.abm.data.dao.ChoferDao
import com.example.colectivosapp.abm.data.entities.ChoferEntity
import com.example.colectivosapp.abm.ui.model.Chofer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChoferRepository @Inject constructor(private val choferDao: ChoferDao){
    val choferes: Flow<List<Chofer>> =
        choferDao.getAllChoferes().map { items ->  items.map {
            Chofer(it.id, it.nombre, it.apellido, it.documento,  it.colectivoId)
        }}

    suspend fun getChoferById(id: Int): Chofer{
        return choferDao.getChoferById(id).toChofer()
    }

    suspend fun insert(chofer: Chofer){
        choferDao.insertChofer(chofer.toEntity())
    }

    suspend fun delete(chofer: Chofer){
        choferDao.deleteChofer(chofer.toEntity())
    }
}

fun Chofer.toEntity(): ChoferEntity{
    return ChoferEntity(this.id,this.nombre, this.apellido, this.documento, this.colectivoId)
}

fun ChoferEntity.toChofer(): Chofer{
    return Chofer(this.id, this.nombre, this.apellido, this.documento, this.colectivoId)
}