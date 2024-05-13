package com.example.colectivosapp.abm.ui.model

data class Colectivo(
    val id: Int = 0,
    val patente: String,
    val lineaId: Int? = null,
    val choferId: Int? = null,
    val recorridoId: Int? = null
){
    override fun toString(): String {
        return "Patente $patente"
    }
}
