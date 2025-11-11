package com.example.reservashotel.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.reservashotel.data.model.Reserva
import com.example.reservashotel.data.repository.ReservasRepository
import com.example.reservashotel.data.repository.HospedesRepository // ⬅️ NOVO: Importe o Repositório de Hóspedes
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ReservaViewModel(
    private val repository: ReservasRepository,
    // 🌟 1. NOVO: Injete o Repositório de Hóspedes
    private val hospedesRepository: HospedesRepository
) : ViewModel() {

    val listaReservas = repository.getAllReservas()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /**
     * Salva (cria ou atualiza) uma reserva.
     */
    fun salvarReserva(
        id: String? = null,
        quartoId: String,
        hospedeId: String,
        nomeCliente: String,
        dataCheckIn: Long,
        dataCheckOut: Long,
        status: String
    ) {
        viewModelScope.launch {
            val reserva = Reserva(
                id = id ?: "",
                quartoId = quartoId,
                hospedeId = hospedeId,
                nomeCliente = nomeCliente,
                dataCheckIn = dataCheckIn,
                dataCheckOut = dataCheckOut,
                status = status
            )

            if (id.isNullOrBlank()) {
                repository.addReserva(reserva)
            } else {
                repository.updateReserva(reserva)
            }
        }
    }

    fun excluirReserva(reserva: Reserva) {
        viewModelScope.launch {
            repository.deleteReserva(reserva)
        }
    }

    // Função de carregamento para a tela de edição
    suspend fun carregarReservaPorId(id: String): Reserva? {
        return repository.getReservaById(id)
    }

    //  2. NOVO: Função para busca reativa do nome do hóspede
    /**
     * Busca o nome do hóspede pelo ID. Usada para preenchimento automático na UI.
     */
    suspend fun buscarNomeHospede(id: String): String? {
        // Converte o ID de String (da UI) para Int (do modelo de dados Hospede)
        val idInt = id.toIntOrNull() ?: return null

        // Chama o Repositório de Hóspedes injetado para buscar
        val hospede = hospedesRepository.getHospedeById(idInt)

        return hospede?.nome
    }


    //  3. CORREÇÃO DO FACTORY: Deve aceitar ambos os Repositórios
    class Factory(
        private val reservasRepository: ReservasRepository,
        private val hospedesRepository: HospedesRepository 
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ReservaViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                // Passa AMBOS os repositórios para o construtor
                return ReservaViewModel(reservasRepository, hospedesRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}