package com.example.colectivosapp.abm.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.colectivosapp.abm.data.entities.ChoferEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChoferDao{

    @Query("SELECT * FROM chofer_table ORDER BY apellido DESC")
    fun getAllChoferes():Flow<List<ChoferEntity>>

    @Query("SELECT * FROM chofer_table WHERE id = :choferId")
    suspend fun getChoferById(choferId:Int):ChoferEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChofer(chofer:ChoferEntity)

    @Delete
    suspend fun deleteChofer(chofer: ChoferEntity)
}