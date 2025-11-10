package com.example.reservashotel.data.repository

import com.example.reservashotel.data.dao.HospedeDao
import com.example.reservashotel.data.model.Hospede
import kotlinx.coroutines.flow.Flow

class HospedesRepository(private val hospedeDao: HospedeDao) {

    fun getAllHospedes(): Flow<List<Hospede>> {
        return hospedeDao.getAll()
    }

    // 🌟 NOVO: Função para buscar o Hóspede por ID
    suspend fun getHospedeById(id: Int): Hospede? {
        return hospedeDao.getHospedeById(id)
    }

    suspend fun addOrUpdateHospede(hospede: Hospede) {
        hospedeDao.insert(hospede)
    }

    suspend fun excluirHospede(hospede: Hospede) {
        hospedeDao.delete(hospede)
    }
}