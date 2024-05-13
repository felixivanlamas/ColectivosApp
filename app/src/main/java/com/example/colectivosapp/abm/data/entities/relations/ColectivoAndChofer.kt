package com.example.colectivosapp.abm.data.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.colectivosapp.abm.data.entities.ChoferEntity
import com.example.colectivosapp.abm.data.entities.ColectivoEntity

data class ColectivoAndChofer(
    @Embedded val colectivo: ColectivoEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "colectivoId"
    )
    val chofer: ChoferEntity
)