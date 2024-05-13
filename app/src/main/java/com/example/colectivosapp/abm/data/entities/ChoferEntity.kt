package com.example.colectivosapp.abm.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chofer_table")
data class ChoferEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val nombre: String,
    val apellido: String,
    val documento: String,
)