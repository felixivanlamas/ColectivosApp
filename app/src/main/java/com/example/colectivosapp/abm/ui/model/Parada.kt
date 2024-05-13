package com.example.colectivosapp.abm.ui.model

data class Parada(
    val id: Int = 0,
    val nombre: String,
    val direccion: String,
){
    override fun toString(): String {
        return "$nombre. Dirección: $direccion"
    }
}