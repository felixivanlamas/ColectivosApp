package com.example.colectivosapp.abm.ui.model

data class Sube(
    val subeId: Long=System.currentTimeMillis().hashCode().toLong(),
)
