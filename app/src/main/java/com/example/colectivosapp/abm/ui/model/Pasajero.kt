package com.example.colectivosapp.abm.ui.model

data class Pasajero(
    val pasajeroId:Int,
    val nombre: String,
    val apellido: String,
    val dni: String,
    var subeId: Long=0
){
    override fun toString(): String {
        return "$nombre, $apellido. DNI:$dni"
    }

}