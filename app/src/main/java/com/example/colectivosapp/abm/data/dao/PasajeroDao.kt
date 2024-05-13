package com.example.colectivosapp.abm.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.colectivosapp.abm.data.entities.PasajeroEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PasajeroDao {
    @Query("SELECT * from pasajero_table ORDER BY nombre ASC")
    fun getAllPasajeros(): Flow<List<PasajeroEntity>>

    @Query("SELECT * FROM pasajero_table WHERE pasajeroId = :pasajeroId")
    suspend fun getPasajeroById(pasajeroId: Int): PasajeroEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPasajero(pasajero: PasajeroEntity)

    @Delete
    suspend fun deletePasajero(pasajero : PasajeroEntity)
}