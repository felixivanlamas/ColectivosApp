package com.example.colectivosapp.abm.data.entities

import androidx.room.Entity

@Entity(tableName = "recorrido_parada", primaryKeys = ["recorridoId", "paradaId"])
data class RecorridoParada (
    val recorridoId: Int,
    val paradaId: Int,
    val orden: Int
)