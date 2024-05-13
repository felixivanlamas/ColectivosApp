package com.example.colectivosapp.abm.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.colectivosapp.abm.data.entities.LineaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LineaDao {

    @Query("SELECT * from linea_table ORDER BY id ASC")
    fun getAllLineas(): Flow<List<LineaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLinea(item: LineaEntity)

    @Delete
    suspend fun deleteLinea(linea : LineaEntity)

    @Query("SELECT * FROM linea_table WHERE id = :lineaId")
    suspend fun getLineaById(lineaId: Int): LineaEntity
}
