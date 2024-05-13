package com.example.colectivosapp.abm.data.entities.relations

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation
import com.example.colectivosapp.abm.data.entities.ParadaEntity
import com.example.colectivosapp.abm.data.entities.RecorridoEntity
import com.example.colectivosapp.abm.data.entities.RecorridoParada

@Entity
data class RecorridoWithParadas (
    @Embedded val recorrido: RecorridoEntity,
    @Relation(
        parentColumn = "recorridoId",
        entityColumn = "paradaId",
        associateBy = Junction(RecorridoParada::class)
    )
    val paradas: List<ParadaEntity>
)