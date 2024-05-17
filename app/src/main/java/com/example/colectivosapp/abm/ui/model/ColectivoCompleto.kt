package com.example.colectivosapp.abm.ui.model

data class ColectivoCompleto (
    val id: Int = 0,
    val patente: String? = null,
    val linea: Linea? = null,
    val chofer: Chofer? = null,
    val recorrido: Recorrido? = null
)