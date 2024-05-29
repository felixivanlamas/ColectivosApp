package com.example.colectivosapp.abm.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "parada_table")
data class ParadaEntity(
    @PrimaryKey(autoGenerate = true)
    val paradaId: Int=0,
    val nombreParada: String?,
    val direccion: String?,
    val latitud: Double?,
    val longitud: Double?
)