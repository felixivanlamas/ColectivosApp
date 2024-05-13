package com.example.colectivosapp.abm.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.colectivosapp.abm.data.entities.RecorridoEntity
import com.example.colectivosapp.abm.data.entities.relations.RecorridoWithParadas
import kotlinx.coroutines.flow.Flow

@Dao
interface RecorridoDao {

    @Query("SELECT * from recorrido_table ORDER BY nombreRecorrido ASC")
    fun getAllRecorridos(): Flow<List<RecorridoEntity>>

    @Transaction
    @Query("SELECT * FROM recorrido_table")
    fun getRecorridosWithParadas(): Flow<List<RecorridoWithParadas>>

    @Transaction
    @Query("SELECT * FROM recorrido_table WHERE recorridoId = :recorridoId")
    suspend fun getRecorridoWithParadas(recorridoId: Int): RecorridoWithParadas

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecorrido(item: RecorridoEntity): Long

    @Delete
    suspend fun deleteRecorrido(linea : RecorridoEntity)

    @Query("SELECT * FROM recorrido_table WHERE recorridoId = :recorridoId")
    suspend fun getRecorridoById(recorridoId: Int): RecorridoEntity
}