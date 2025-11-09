package com.example.reservashotel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.reservashotel.ui.theme.ReservasHotelTheme
import com.example.reservashotel.view.FormularioReservaScreen
import com.example.reservashotel.view.ListaReservasScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ReservasHotelTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "listaReservas") {
        composable("listaReservas") {
            ListaReservasScreen(navController)
        }
        composable("formularioReserva") {
            FormularioReservaScreen(navController)
        }
    }
}
