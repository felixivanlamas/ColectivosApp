package com.example.colectivosapp.abm.data.repositories

import com.example.colectivosapp.abm.data.dao.ColectivoDao
import com.example.colectivosapp.abm.data.entities.ColectivoEntity
import com.example.colectivosapp.abm.ui.model.Colectivo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ColectivoRepository @Inject constructor(private val colectivoDao: ColectivoDao) {
    val colectivos: Flow<List<Colectivo>> =
        colectivoDao.getAllColectivos().map { items -> items.map {
            Colectivo(
                id = it.id,
                patente = it.patente!!,
                lineaId = it.lineaId ,
                choferId = it.choferId,
                recorridoId = it.recorridoId)
        }}

    fun getColectivosByLineaId(lineaId: Int): Flow<List<Colectivo>> =
        colectivoDao.getColectivosByLineaId(lineaId).map {
            items -> items.map {
                Colectivo(
                    id = it.id,
                    patente = it.patente!!,
                    lineaId = it.lineaId ,
                    choferId = it.choferId,
                    recorridoId = it.recorridoId)
            }
        }

    suspend fun getColectivoById(id: Int): Colectivo {
        return colectivoDao.getColectivoById(id).toColectivo()
    }

    suspend fun add(colectivo: Colectivo) {
        colectivoDao.insertColectivo(colectivo.toEntity())
    }

    suspend fun update(colectivo: Colectivo) {
        colectivoDao.insertColectivo(colectivo.toEntity())
    }

    suspend fun delete(colectivo: Colectivo) {
        colectivoDao.deleteColectivo(colectivo.toEntity())
    }
}

fun Colectivo.toEntity(): ColectivoEntity {
    return ColectivoEntity(this.id, this.patente, this.lineaId, this.choferId, this.recorridoId)
}
fun ColectivoEntity.toColectivo(): Colectivo {
    return Colectivo(this.id, this.patente!!, this.lineaId, this.choferId, this.recorridoId)
}