package com.example.colectivosapp.abm.data.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.colectivosapp.abm.data.entities.ColectivoEntity
import com.example.colectivosapp.abm.data.entities.LineaEntity
import com.example.colectivosapp.abm.ui.model.Colectivo


data class LineaWithColectivos(
    @Embedded val linea: LineaEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "lineaId"
    )
    val colectivos: List<ColectivoEntity>
)