package com.example.colectivosapp.abm.di

import android.content.Context
import androidx.room.Room
import com.example.colectivosapp.abm.data.ColectivosAppDatabase
import com.example.colectivosapp.abm.data.dao.ChoferDao
import com.example.colectivosapp.abm.data.dao.ColectivoDao
import com.example.colectivosapp.abm.data.dao.HistorialDao
import com.example.colectivosapp.abm.data.dao.LineaDao
import com.example.colectivosapp.abm.data.dao.ParadaDao
import com.example.colectivosapp.abm.data.dao.PasajeroDao
import com.example.colectivosapp.abm.data.dao.RecorridoDao
import com.example.colectivosapp.abm.data.dao.RecorridoParadaDao
import com.example.colectivosapp.abm.data.dao.SubeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val APP_DATABASE_NAME = "colectivos_database"

    @Provides
    @Singleton
    fun provideLineaDao(colectivosAppDatabase: ColectivosAppDatabase): LineaDao {
        return colectivosAppDatabase.getLineaDao()
    }

    @Provides
    @Singleton
    fun provideColectivoDao(colectivosAppDatabase: ColectivosAppDatabase): ColectivoDao {
        return colectivosAppDatabase.getColectivoDao()
    }

    @Provides
    @Singleton
    fun provideChoferDao(colectivosAppDatabase: ColectivosAppDatabase): ChoferDao {
        return colectivosAppDatabase.getChoferDao()
    }

    @Provides
    @Singleton
    fun provideParadaDao(colectivosAppDatabase: ColectivosAppDatabase): ParadaDao {
        return colectivosAppDatabase.getParadaDao()
    }

    @Provides
    @Singleton
    fun provideRecorridoDao(colectivosAppDatabase: ColectivosAppDatabase): RecorridoDao {
        return colectivosAppDatabase.getRecorridoDao()
    }

    @Provides
    @Singleton
    fun provideRecorridoParadaDao(colectivosAppDatabase: ColectivosAppDatabase): RecorridoParadaDao {
        return colectivosAppDatabase.getRecorridoParadaDao()
    }

    @Provides
    @Singleton
    fun providePasajeroDao(colectivosAppDatabase: ColectivosAppDatabase): PasajeroDao {
        return colectivosAppDatabase.getPasajeroDao()
    }

    @Provides
    @Singleton
    fun provideSubeDao(colectivosAppDatabase: ColectivosAppDatabase): SubeDao {
        return colectivosAppDatabase.getSubeDao()
    }

    @Provides
    @Singleton
    fun provideHistorialDao(colectivosAppDatabase: ColectivosAppDatabase): HistorialDao {
        return colectivosAppDatabase.getHistorialDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): ColectivosAppDatabase {
        return Room.databaseBuilder(
            appContext,
            ColectivosAppDatabase::class.java,
            APP_DATABASE_NAME
        ).build()
    }
}