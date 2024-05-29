package com.example.colectivosapp.abm.ui.model

data class Parada(
    val id: Int = 0,
    val nombre: String?,
    val direccion: String?,
    val latitud: Double?,
    val longitud: Double?
){
    constructor() : this(id = 0, nombre= "", direccion="", latitud=0.0, longitud=0.0)
    override fun toString(): String {
        return "$nombre. Dirección: $direccion"
    }
}