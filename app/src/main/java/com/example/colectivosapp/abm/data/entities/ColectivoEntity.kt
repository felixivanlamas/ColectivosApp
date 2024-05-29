package com.example.colectivosapp.abm.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey

@Entity(tableName = "colectivo_table", foreignKeys = [
    ForeignKey(entity = ChoferEntity::class, parentColumns = ["id"], childColumns = ["choferId"], onDelete = CASCADE, onUpdate = CASCADE)
])
data class ColectivoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val patente: String? = null,
    val lineaId: Int? = null,
    val choferId: Int? = null,
    val recorridoId: Int? = null
)