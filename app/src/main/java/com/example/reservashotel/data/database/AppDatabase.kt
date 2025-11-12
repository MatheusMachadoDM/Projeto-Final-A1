package com.example.reservashotel.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.reservashotel.data.dao.HospedeDao
import com.example.reservashotel.data.dao.QuartoDao
import com.example.reservashotel.data.dao.ReservaDao
import com.example.reservashotel.data.dao.UsuarioDao
import com.example.reservashotel.data.model.Quarto
import com.example.reservashotel.data.model.Reserva
import com.example.reservashotel.data.model.Hospede
import com.example.reservashotel.data.model.Usuario

/**
 * Define a classe abstrata principal do banco de dados Room.
 * É a peça central da camada de persistência local.
 */
@Database(
    // Lista todas as entidades (tabelas) que farão parte deste banco de dados.
    entities = [Quarto::class, Reserva::class, Hospede::class, Usuario::class],

    // A versão atual do esquema do banco de dados.
    // Deve ser incrementada sempre que a estrutura das entidades mudar.
    version = 6,

    // Define se o Room deve exportar o esquema para uma pasta em disco.
    // Geralmente 'false' em projetos pequenos ou de demonstração.
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // --- Data Access Objects (DAOs) ---
    // Funções abstratas que expõem os DAOs para que o Room possa implementá-los.

    /** Expõe o DAO para operações relacionadas à entidade Quarto. */
    abstract fun quartoDao(): QuartoDao

    /** Expõe o DAO para operações relacionadas à entidade Reserva. */
    abstract fun reservaDao(): ReservaDao

    /** Expõe o DAO para operações relacionadas à entidade Hóspede. */
    abstract fun hospedeDao(): HospedeDao

    /** Expõe o DAO para operações relacionadas à entidade Usuário (Login). */
    abstract fun usuarioDao(): UsuarioDao

    // --- Companion Object ---
    // Contém membros estáticos (constantes) associados à classe do banco de dados.
    companion object {
        /** Define o nome do arquivo do banco de dados no dispositivo. */
        const val DATABASE_NAME = "reservas_hotel_db"
    }
}