package com.example.colectivosapp.abm.ui.model

data class Linea(
    var id: Int,
    var nombre: String,
    var colectivos: List<Colectivo>? = null
){
    constructor() : this(0, "")
    override fun toString(): String {
        return nombre
    }
}