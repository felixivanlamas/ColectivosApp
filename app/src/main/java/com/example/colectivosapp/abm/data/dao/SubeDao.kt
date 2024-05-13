package com.example.colectivosapp.abm.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.colectivosapp.abm.data.entities.SubeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SubeDao {
    @Query("SELECT * FROM sube_table ORDER BY subeId DESC")
    fun getAllSube(): Flow<List<SubeEntity>>

    @Query("SELECT * FROM sube_table WHERE subeId = :subeId")
    suspend fun getSubeById(subeId:Long): SubeEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSube(sube: SubeEntity):Long

    @Delete
    suspend fun deleteSube(sube: SubeEntity)
}