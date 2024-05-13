package com.example.colectivosapp.abm.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.colectivosapp.abm.data.entities.ParadaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ParadaDao{

    @Query("SELECT * from parada_table ORDER BY nombreParada ASC")
    fun getAllParadas(): Flow<List<ParadaEntity>>

    @Query("SELECT * FROM parada_table WHERE paradaId = :paradaId")
    suspend fun getparadaById(paradaId: Int): ParadaEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParada(parada: ParadaEntity)

    @Delete
    suspend fun deleteParada(parada : ParadaEntity)
}
