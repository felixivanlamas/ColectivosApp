package com.example.colectivosapp.abm.ui.model

class Historial(
    val id: Long?=0,
    val pasajeroId: Int,
    val subeId: Long,
    val paradaSubidaId: Int,
    val paradaBajadaId: Int,
    val fecha: String
)