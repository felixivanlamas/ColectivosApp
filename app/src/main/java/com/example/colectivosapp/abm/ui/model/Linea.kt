package com.example.colectivosapp.abm.ui.model

data class Linea(
    var id: Int,
    var nombre: String,
    var colectivos: List<Colectivo>? = null
){
    override fun toString(): String {
        return nombre
    }
}