package com.example.colectivosapp.abm.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "linea_table")
data class LineaEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val nombre: String,
)