package com.example.reservashotel.data.repository

import com.example.reservashotel.data.dao.QuartoDao
import com.example.reservashotel.data.dao.ReservaDao
import com.example.reservashotel.data.model.Reserva
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import java.util.UUID

class ReservasRepository(
    private val quartoDao: QuartoDao, // Mantido para futuras validações (RF12)
    private val reservaDao: ReservaDao,
    private val firestore: FirebaseFirestore
) {

    // --- RESERVAS ---
    fun getAllReservas(): Flow<List<Reserva>> = reservaDao.getAllReservas()

    suspend fun addReserva(reserva: Reserva) {
        // Validação de Conflito de Datas (RF12) DEVE ser implementada aqui

        // Garante que a reserva tenha um ID único (UUID ou Firestore ID)
        val reservaComId = if (reserva.id.isBlank()) reserva.copy(id = UUID.randomUUID().toString()) else reserva

        // 1. Persistência Local (Room)
        reservaDao.insertReserva(reservaComId)

        // 2. Sincronização em Nuvem (Firestore)
        firestore.collection("reservations").document(reservaComId.id).set(reservaComId).await()
    }

    suspend fun updateReserva(reserva: Reserva) {
        // 1. Persistência Local (Room)
        reservaDao.updateReserva(reserva)

        // 2. Sincronização em Nuvem (Firestore)
        firestore.collection("reservations").document(reserva.id).set(reserva).await()
    }

    suspend fun deleteReserva(reserva: Reserva) {
        // 1. Exclusão Local (Room)
        reservaDao.deleteReserva(reserva)

        // 2. Exclusão em Nuvem (Firestore)
        firestore.collection("reservations").document(reserva.id).delete().await()
    }

    suspend fun getReservaById(id: String): Reserva? = reservaDao.getReservaById(id)
}