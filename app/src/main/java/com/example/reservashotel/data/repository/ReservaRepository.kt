package com.example.reservashotel.repository

import com.example.reservashotel.data.dao.ClienteDao
import com.example.reservashotel.data.dao.ReservaDao
import com.example.reservashotel.data.model.Cliente
import com.example.reservashotel.model.Reserva
import kotlinx.coroutines.flow.Flow

class ReservaRepository(
    private val clienteDao: ClienteDao,
    private val reservaDao: ReservaDao
) {
    fun listarClientes(): Flow<List<Cliente>> = clienteDao.listarClientes()
    fun listarReservas(): Flow<List<Reserva>> = reservaDao.listarReservas()

    suspend fun inserirCliente(cliente: Cliente) = clienteDao.inserir(cliente)
    suspend fun inserirReserva(reserva: Reserva) = reservaDao.inserir(reserva)

    suspend fun atualizarCliente(cliente: Cliente) = clienteDao.atualizar(cliente)
    suspend fun atualizarReserva(reserva: Reserva) = reservaDao.atualizar(reserva)

    suspend fun deletarCliente(cliente: Cliente) = clienteDao.deletar(cliente)
    suspend fun deletarReserva(reserva: Reserva) = reservaDao.deletar(reserva)
}
