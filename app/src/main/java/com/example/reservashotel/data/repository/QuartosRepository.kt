package com.example.reservashotel.data.repository

import com.example.reservashotel.data.dao.QuartoDao
import com.example.reservashotel.data.model.Quarto
import kotlinx.coroutines.flow.Flow

class QuartosRepository(private val quartoDao: QuartoDao) {

    // CORRIGIDO: Retorna Flow<List<Quarto>> do DAO
    fun getAllQuartos(): Flow<List<Quarto>> = quartoDao.getAll()

    // CORRIGIDO: Aceita o objeto Quarto inteiro, conforme o QuartoViewModel
    suspend fun addOrUpdateQuarto(quarto: Quarto) {
        quartoDao.insert(quarto)
    }

    suspend fun excluirQuarto(quarto: Quarto) {
        quartoDao.delete(quarto)
    }

    suspend fun getQuartoById(id: String): Quarto? {
        return quartoDao.getQuartoById(id.toString())
    }
}