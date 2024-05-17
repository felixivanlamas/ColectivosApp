package com.example.colectivosapp.abm.ui.model

data class Chofer(
    val id:Int,
    val nombre: String,
    val apellido: String,
    val documento: String
){
    constructor(): this(0, "", "", "")
    @Override
    override fun toString(): String {
        return "$nombre $apellido"
    }
}
