package com.example.colectivosapp.abm.ui.model

data class Recorrido(
    val id: Int = 0,
    val nombre: String,
    val paradas: List<ParadaOrden>? = null,
){
    constructor() : this(0, "")
    @Override
    override fun toString(): String {
        return nombre
    }
}