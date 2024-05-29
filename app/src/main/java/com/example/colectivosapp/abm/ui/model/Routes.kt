package com.example.colectivosapp.abm.ui.model

sealed class Routes (val route:String){
    object HomeScreen: Routes("homeScreen")
    object AbmLineas: Routes("abmLineas")
    object AbmColectivos: Routes("abmColectivos")
    object AbmChoferes: Routes("abmChoferes")
    object AbmRecorridos: Routes("abmRecorridos")
    object AbmParadas: Routes("abmParadas")
    object LineaDetail: Routes("lineaDetail/{lineaId}"){
        fun createRoute(lineaId:Int) = "lineaDetail/$lineaId"
    }
    object ColectivoDetail: Routes("colectivoDetail/{colectivoId}"){
        fun createRoute(colectivoId:Int) = "colectivoDetail/$colectivoId"
    }
    object ChoferDetail: Routes("choferDetail/{choferId}"){
        fun createRoute(choferId:Int) = "choferDetail/$choferId"
    }
    object RecorridoDetail: Routes("recorridoDetail/{recorridoId}"){
        fun createRoute(recorridoId:Int) = "recorridoDetail/$recorridoId"
    }
    object ParadaDetail: Routes("paradaDetail/{paradaId}"){
        fun createRoute(paradaId:Int) = "paradaDetail/$paradaId"
    }
    object RegistroPasajero: Routes("registroPasajero")
    object SimulacionScreen: Routes("simulacionScreen")
    object HistorialScreen: Routes("historialScreen")
}