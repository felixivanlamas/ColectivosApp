package com.example.colectivosapp.abm.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavController

@Composable
fun ColectivoDetailScreen(colectivoId: Int, colectivoDetailViewModel: ColectivoDetailViewModel, navigationController: NavController){
    val showDialog: Boolean by colectivoDetailViewModel.showDialog.observeAsState(initial = false)
    Text(text = colectivoId.toString())

}