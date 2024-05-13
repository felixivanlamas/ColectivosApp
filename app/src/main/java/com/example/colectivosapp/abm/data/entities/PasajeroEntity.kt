package com.example.colectivosapp.abm.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pasajero_table")
data class PasajeroEntity (
    @PrimaryKey(autoGenerate = false)
    val pasajeroId:Int,
    val nombre: String,
    val apellido: String,
    val dni: String,
    val subeId: Long
){}