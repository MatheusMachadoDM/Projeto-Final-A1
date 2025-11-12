package com.example.reservashotel.data.model

import androidx.room.Entity // Importa a anotação para definir uma tabela Room.
import androidx.room.PrimaryKey // Importa a anotação para definir a chave primária.

/**
 * Classe de modelo de dados (Data Class) que representa uma tabela no banco de dados Room.
 * Esta classe define a estrutura da tabela 'quartos'.
 *
 * @param id Identificador único do quarto (Chave Primária).
 * @param numero Número do quarto.
 * @param tipo Tipo do quarto (Ex: Casal, Solteiro, Luxo).
 * @param valorDiaria Preço da diária do quarto.
 * @param status Status atual do quarto (Ex: Disponível, Ocupado, Manutenção).
 */
@Entity(tableName = "quartos")
data class Quarto(
    // Define 'id' como a chave primária da tabela.
    // 'autoGenerate = true' indica que o Room deve gerar automaticamente
    // um valor único para o ID em novas inserções (começando em 1).
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val numero: Int,
    val tipo: String,
    val valorDiaria: Double,
    val status: String
)