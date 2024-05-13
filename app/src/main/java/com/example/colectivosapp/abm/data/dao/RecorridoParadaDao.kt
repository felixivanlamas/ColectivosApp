package com.example.colectivosapp.abm.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.colectivosapp.abm.data.entities.RecorridoParada

@Dao
interface RecorridoParadaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecorridoParada(recorridoParada: RecorridoParada)

//    @Query("SELECT * FROM parada_table INNER JOIN recorrido_parada ON paradaId = recorrido_parada.paradaId WHERE recorridoId = :recorridoId ORDER BY orden ASC")
//    suspend fun getParadasFromRecorrido(recorridoId: Int): List<RecorridoParada>

    @Query("SELECT * FROM parada_table INNER JOIN recorrido_parada ON parada_table.paradaId = recorrido_parada.paradaId WHERE recorrido_parada.recorridoId = :recorridoId ORDER BY recorrido_parada.orden ASC")
    suspend fun getParadasFromRecorrido(recorridoId: Int): List<RecorridoParada>

    @Query("DELETE FROM recorrido_table WHERE recorridoId = :recorridoId")
    suspend fun deleteParadasForRecorrido(recorridoId: Int)

//    @Query("SELECT * FROM recorrido_table INNER JOIN RecorridoParada ON recorridoId = RecorridoParada.recorridoId WHERE RecorridoParada.paradaId = :paradaId")
//    suspend fun getRecorridosForParada(paradaId: Long): List<Recorrido>
}