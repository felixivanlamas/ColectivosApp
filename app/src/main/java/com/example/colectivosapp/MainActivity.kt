package com.example.colectivosapp

//import com.example.colectivosapp.abm.ui.LineaDetailScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.colectivosapp.abm.ui.AbmChoferScreen
import com.example.colectivosapp.abm.ui.AbmChoferScreenViewModel
import com.example.colectivosapp.abm.ui.AbmColectivoScreen
import com.example.colectivosapp.abm.ui.AbmColectivoViewModel
import com.example.colectivosapp.abm.ui.AbmLineaScreen
import com.example.colectivosapp.abm.ui.AbmLineaViewModel
import com.example.colectivosapp.abm.ui.AbmParadaScreen
import com.example.colectivosapp.abm.ui.AbmParadaViewModel
import com.example.colectivosapp.abm.ui.AbmRecorridoScreen
import com.example.colectivosapp.abm.ui.AbmRecorridoViewModel
import com.example.colectivosapp.abm.ui.ColectivoDetailScreen
import com.example.colectivosapp.abm.ui.ColectivoDetailViewModel
import com.example.colectivosapp.abm.ui.LineaDetailScreen
import com.example.colectivosapp.abm.ui.LineaDetailViewModel
import com.example.colectivosapp.abm.ui.PasajeroRegister
import com.example.colectivosapp.abm.ui.PasajeroRegisterViewModel
import com.example.colectivosapp.abm.ui.SimulacionScreen
import com.example.colectivosapp.abm.ui.SimulacionViewModel
import com.example.colectivosapp.abm.ui.model.Routes
import com.example.colectivosapp.ui.theme.ColectivosAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val abmLineaViewModel: AbmLineaViewModel by viewModels()
    private val lineaDetailViewModel: LineaDetailViewModel by viewModels()
    private val abmColectivoViewModel: AbmColectivoViewModel by viewModels()
    private val colectivoDetailViewModel: ColectivoDetailViewModel by viewModels()
    private val chofereScreenViewModel: AbmChoferScreenViewModel by viewModels()
    private val abmParadaViewModel: AbmParadaViewModel by viewModels()
    private val abmRecorridoViewModel: AbmRecorridoViewModel by viewModels()
    private val pasajeroRegisterViewModel: PasajeroRegisterViewModel by viewModels()
    private val simulacionViewModel: SimulacionViewModel by viewModels()

    private val navItemList = listOf(
        NavigationItem("Colectivos App",R.drawable.homenaviconfilled, R.drawable.homenaviconoutlined,"homeScreen"),
        NavigationItem("Lineas", R.drawable.lineanavicon, R.drawable.lineanavicon, "abmLineas"),
        NavigationItem("Colectivos", R.drawable.colectivofillednavicon, R.drawable.colectivooutlinednavicon, "abmColectivos"),
        NavigationItem("Choferes", R.drawable.chofernavicon, R.drawable.chofernavicon, "abmChoferes"),
        NavigationItem("Paradas", R.drawable.paradanaviconfilled, R.drawable.paradanaviconoutlined, "abmParadas"),
        NavigationItem("Recorridos", R.drawable.recorridonaviconfilled, R.drawable.recorridonaviconoutlined, "abmRecorridos")
    )

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ColectivosAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navigationController = rememberNavController()
                    var title by rememberSaveable {
                        mutableStateOf("Colectivos App")
                    }
                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                    val scope = rememberCoroutineScope()
                    var selectedItemIndex by rememberSaveable {
                        mutableIntStateOf(0)
                    }
                    ModalNavigationDrawer(
                        drawerContent = {
                            ModalDrawerSheet {
                                Spacer(modifier = Modifier.height(16.dp))
                                navItemList.forEachIndexed { index, navigationItem ->
                                    NavigationDrawerItem(
                                        label = {
                                            Text(text = navigationItem.title)
                                        },
                                        selected = index == selectedItemIndex,
                                        onClick = {
                                            navigationController.navigate(navigationItem.route)
                                            title = navigationItem.title
                                            selectedItemIndex = index
                                            scope.launch {
                                                drawerState.close()
                                            }
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = ImageVector.vectorResource(
                                                    if (index == selectedItemIndex) {
                                                        navigationItem.selectedIcon
                                                    } else {
                                                        navigationItem.unSelectedIcon
                                                    }
                                                ),
                                                contentDescription = navigationItem.title
                                            )
                                        },
                                        modifier = Modifier
                                            .padding(NavigationDrawerItemDefaults.ItemPadding)
                                    )
                                }
                            }
                        },
                        drawerState = drawerState
                    ) {
                        Scaffold(topBar = {
                            CenterAlignedTopAppBar(
                                title = {
                                    Text(text = title, maxLines = 1)
                                },
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    titleContentColor = MaterialTheme.colorScheme.primary,
                                ),
                                navigationIcon = {
                                    IconButton(onClick = {
                                        scope.launch {
                                            drawerState.open()
                                        }
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Menu,
                                            contentDescription = "Menu"
                                        )
                                    }
                                }
                            )
                        }) {
                            NavHost(
                                modifier = Modifier.padding(it),
                                navController = navigationController,
                                startDestination = Routes.HomeScreen.route
                            ) {
                                composable(Routes.HomeScreen.route) { HomeScreen(navigationController) };
                                composable(Routes.RegistroPasajero.route) {
                                    PasajeroRegister(
                                        pasajeroRegisterViewModel,
                                        navigationController
                                    )
                                }
                                composable(Routes.AbmLineas.route) {
                                    AbmLineaScreen(
                                        abmLineaViewModel,
                                        navigationController
                                    )
                                }
                                composable(Routes.AbmColectivos.route) {
                                    AbmColectivoScreen(
                                        abmColectivoViewModel,
                                        navigationController
                                    )
                                }
                                composable(Routes.AbmChoferes.route) {
                                    AbmChoferScreen(
                                        chofereScreenViewModel,
                                        navigationController
                                    )
                                }
                                composable(Routes.AbmParadas.route) {
                                    AbmParadaScreen(
                                        abmParadaViewModel,
                                        navigationController
                                    )
                                }
                                composable(Routes.AbmRecorridos.route) {
                                    AbmRecorridoScreen(
                                        abmRecorridoViewModel,
                                        navigationController
                                    )
                                }
                                composable(Routes.SimulacionScreen.route) {
                                    SimulacionScreen(
                                        simulacionViewModel,
                                        navigationController
                                    )
                                }
                                composable(
                                    Routes.LineaDetail.route,
                                    arguments = listOf(navArgument("lineaId") {
                                        type = NavType.IntType
                                    })
                                ) { navBackStackEntry ->
                                    LineaDetailScreen(
                                        navBackStackEntry.arguments?.getInt("lineaId") ?: 0,
                                        lineaDetailViewModel,
                                        navigationController
                                    )
                                }
                                composable(
                                    Routes.ColectivoDetail.route,
                                    arguments = listOf(navArgument("colectivoId") {
                                        type = NavType.IntType
                                    })
                                ) { navBackStackEntry ->
                                    ColectivoDetailScreen(
                                        navBackStackEntry.arguments?.getInt("colectivoId") ?: 0,
                                        colectivoDetailViewModel,
                                        navigationController,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}