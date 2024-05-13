package com.example.colectivosapp.abm.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "historial_table")
data class HistorialEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long =0,
    val pasajeroId: Int,
    val subeId: Long,
    val paradaSubidaId: Int,
    val paradaBajadaId: Int,
    val fecha: String
)