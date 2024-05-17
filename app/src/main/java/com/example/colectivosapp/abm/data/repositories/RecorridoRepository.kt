package com.example.colectivosapp.abm.data.repositories

import android.util.Log
import com.example.colectivosapp.abm.data.dao.RecorridoDao
import com.example.colectivosapp.abm.data.dao.RecorridoParadaDao
import com.example.colectivosapp.abm.data.entities.RecorridoEntity
import com.example.colectivosapp.abm.data.entities.RecorridoParada
import com.example.colectivosapp.abm.ui.model.ParadaOrden
import com.example.colectivosapp.abm.ui.model.Recorrido
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecorridoRepository @Inject constructor(private val recorridoDao: RecorridoDao, private val recorridoParadaDao: RecorridoParadaDao){
    val recorridos: Flow<List<Recorrido>> =
        recorridoDao.getAllRecorridos().map { items -> items.map { it.toRecorrido() } }

    suspend fun getRecorridoWithParadas(recorridoId: Int): Recorrido {
        // Obtener el recorrido con sus paradas
        val recorrido = recorridoDao.getRecorridoWithParadas(recorridoId)
        // Obtener la lista de paradas con sus órdenes
        val listaParadasOrden = recorridoParadaDao.getParadasFromRecorrido(recorridoId)

        // Combina el recorrido con sus paradas y la lista de paradas con sus órdenes
        val recorridoConParadasOrdenadas = recorrido.paradas.map { parada ->
            val orden = listaParadasOrden.find { it.paradaId == parada.paradaId }?.orden ?: 0
            ParadaOrden(parada.toParada(), orden)
        }

        // Ordena la lista combinada por el campo de orden
        val paradasOrdenadas = recorridoConParadasOrdenadas.sortedBy { it.orden }

        // Crea un nuevo objeto Recorrido con la lista de paradas ordenadas
        return Recorrido(recorrido.recorrido.recorridoId, recorrido.recorrido.nombreRecorrido, paradasOrdenadas)
    }

    suspend fun getRecorridoById(id: Int) : Recorrido {
        return recorridoDao.getRecorridoById(id).toRecorrido()
    }

    suspend fun insertRecorrido(recorrido: Recorrido, paradasIds: List<Int>) {
        try {
            val recorridoId = recorridoDao.insertRecorrido(recorrido.toRecorridoEntity())
            var i = 0
            paradasIds.forEach { paradaId ->
                recorridoParadaDao.insertRecorridoParada(RecorridoParada(recorridoId = recorridoId.toInt(), paradaId = paradaId, orden = i))
                i++
            }
        } catch (e: Exception) {
            Log.e("ERROR RECORRIDO", "Error al insertar el recorrido: $e")
        }
    }

    suspend fun deleteRecorrido(recorrido: Recorrido) {
        recorridoDao.deleteRecorrido(recorrido.toRecorridoEntity())
    }

}

fun RecorridoEntity.toRecorrido() = Recorrido(recorridoId, nombreRecorrido)

fun Recorrido.toRecorridoEntity() = RecorridoEntity(id, nombre)