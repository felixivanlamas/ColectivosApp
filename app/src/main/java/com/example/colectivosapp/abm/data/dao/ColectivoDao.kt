package com.example.colectivosapp.abm.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.colectivosapp.abm.data.entities.ColectivoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ColectivoDao {

    @Query("SELECT * FROM colectivo_table ORDER BY patente ASC")
    fun getAllColectivos(): Flow<List<ColectivoEntity>>

    @Query("SELECT * FROM colectivo_table WHERE lineaId = :lineaId ORDER BY patente ASC")
    fun getColectivosByLineaId(lineaId: Int): Flow<List<ColectivoEntity>>

    @Query("SELECT * FROM colectivo_table WHERE id = :id")
    suspend fun getColectivoById(id: Int): ColectivoEntity



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertColectivo(item: ColectivoEntity)

    @Delete
    suspend fun deleteColectivo(colectivo: ColectivoEntity)
}