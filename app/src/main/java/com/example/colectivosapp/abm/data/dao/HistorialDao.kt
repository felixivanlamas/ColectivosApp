package com.example.colectivosapp.abm.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.colectivosapp.abm.data.entities.HistorialEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistorialDao {
    @Query("SELECT * from historial_table ORDER BY fecha DESC")
    fun getAllHistorial(): Flow<List<HistorialEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistorial(item: HistorialEntity)

    @Delete
    suspend fun deleteHistorial(linea : HistorialEntity)
}