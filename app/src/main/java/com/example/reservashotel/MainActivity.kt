package com.example.reservashotel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import com.example.reservashotel.data.database.AppDatabase
import com.example.reservashotel.data.repository.ReservasRepository
import com.example.reservashotel.data.repository.QuartosRepository // Importação Adicionada
import com.example.reservashotel.ui.theme.ReservasHotelTheme
import com.example.reservashotel.ui.view.screens.*
import com.example.reservashotel.ui.viewmodel.QuartoViewModel
import com.example.reservashotel.ui.viewmodel.ReservaViewModel
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // --- 1. Inicialização do Banco de Dados Room ---
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).build()

        // --- 2. Inicialização do Firebase Firestore ---
        val firestore = FirebaseFirestore.getInstance()

        // --- 3. Inicialização dos Repositórios ---
        // Repositório de Quartos (agora separado e injetado)
        val quartosRepository = QuartosRepository(
            quartoDao = db.quartoDao()
        )

        // Repositório de Reservas (usa DAOs e Firestore)
        val reservasRepository = ReservasRepository(
            quartoDao = db.quartoDao(), // Mantém acesso ao QuartoDao para futuras validações (RF12)
            reservaDao = db.reservaDao(),
            firestore = firestore
        )

        // --- 4. Inicialização dos ViewModels ---
        // Injeta o Repositório CORRETO para cada ViewModel.
        val quartoViewModel: QuartoViewModel by viewModels {
            QuartoViewModel.Factory(quartosRepository) // Injeta QuartosRepository
        }
        val reservaViewModel: ReservaViewModel by viewModels {
            ReservaViewModel.Factory(reservasRepository) // Injeta ReservasRepository
        }

        setContent {
            ReservasHotelTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    AppNavigation(
                        quartoViewModel = quartoViewModel,
                        reservaViewModel = reservaViewModel
                    )
                }
            }
        }
    }
}

// --- 5. Componente de Navegação ---
@Composable
fun AppNavigation(
    quartoViewModel: QuartoViewModel,
    reservaViewModel: ReservaViewModel
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(navController)
        }

        // --- Quartos ---
        composable("lista_quartos") {
            ListaQuartosScreen(navController, quartoViewModel)
        }

        composable(
            "form_quarto?id={id}",
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            FormQuartoScreen(navController, quartoViewModel, id)
        }

        // --- Reservas ---
        composable("lista_reservas") {
            ListaReservasScreen(navController, reservaViewModel)
        }

        composable(
            "form_reserva?id={id}",
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            FormReservaScreen(navController, reservaViewModel, id)
        }

        // Rotas para Hospede e Login/Logout devem ser adicionadas aqui futuramente
    }
}