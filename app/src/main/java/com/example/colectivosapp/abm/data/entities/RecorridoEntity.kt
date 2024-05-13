package com.example.colectivosapp.abm.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recorrido_table")
data class RecorridoEntity (
    @PrimaryKey(autoGenerate = true)
    val recorridoId: Int = 0,
    val nombreRecorrido: String
)