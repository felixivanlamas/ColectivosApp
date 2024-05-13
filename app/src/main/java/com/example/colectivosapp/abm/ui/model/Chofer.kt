package com.example.colectivosapp.abm.ui.model

data class Chofer(
    val id:Int,
    val nombre: String,
    val apellido: String,
    val documento: String
){
    override fun toString(): String {
        return "$nombre $apellido"
    }
}
