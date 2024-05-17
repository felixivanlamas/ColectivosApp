package com.example.colectivosapp.abm.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "colectivo_table")
data class ColectivoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val patente: String? = null,
    val lineaId: Int? = null,
    val choferId: Int? = null,
    val recorridoId: Int? = null
)