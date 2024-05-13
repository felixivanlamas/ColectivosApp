package com.example.colectivosapp.abm.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.colectivosapp.abm.data.dao.ChoferDao
import com.example.colectivosapp.abm.data.dao.ColectivoDao
import com.example.colectivosapp.abm.data.dao.HistorialDao
import com.example.colectivosapp.abm.data.dao.LineaDao
import com.example.colectivosapp.abm.data.dao.ParadaDao
import com.example.colectivosapp.abm.data.dao.PasajeroDao
import com.example.colectivosapp.abm.data.dao.RecorridoDao
import com.example.colectivosapp.abm.data.dao.RecorridoParadaDao
import com.example.colectivosapp.abm.data.dao.SubeDao
import com.example.colectivosapp.abm.data.entities.ChoferEntity
import com.example.colectivosapp.abm.data.entities.ColectivoEntity
import com.example.colectivosapp.abm.data.entities.HistorialEntity
import com.example.colectivosapp.abm.data.entities.LineaEntity
import com.example.colectivosapp.abm.data.entities.ParadaEntity
import com.example.colectivosapp.abm.data.entities.PasajeroEntity
import com.example.colectivosapp.abm.data.entities.RecorridoEntity
import com.example.colectivosapp.abm.data.entities.RecorridoParada
import com.example.colectivosapp.abm.data.entities.SubeEntity

@Database(entities = [LineaEntity::class, ColectivoEntity::class, ChoferEntity::class, ParadaEntity::class, RecorridoEntity::class, RecorridoParada::class, PasajeroEntity::class, SubeEntity::class, HistorialEntity::class], version = 1)
abstract class ColectivosAppDatabase:RoomDatabase(){
    //DAO
    abstract fun getLineaDao():LineaDao
    abstract fun getColectivoDao():ColectivoDao
    abstract fun getChoferDao():ChoferDao
    abstract fun getParadaDao(): ParadaDao
    abstract fun getRecorridoDao(): RecorridoDao
    abstract fun getRecorridoParadaDao(): RecorridoParadaDao
    abstract fun getPasajeroDao(): PasajeroDao
    abstract fun getSubeDao(): SubeDao
    abstract fun getHistorialDao(): HistorialDao
}