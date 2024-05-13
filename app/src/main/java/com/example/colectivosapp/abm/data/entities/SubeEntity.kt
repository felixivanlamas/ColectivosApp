package com.example.colectivosapp.abm.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sube_table")
data class SubeEntity (
    @PrimaryKey(autoGenerate = false)
    val subeId: Long
)