package com.example.reservashotel.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.reservashotel.data.model.Reserva
import com.example.reservashotel.data.repository.ReservasRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ReservaViewModel(private val repository: ReservasRepository) : ViewModel() {

    // Expõe a lista como StateFlow, observando o repositório (correto para Compose)
    val listaReservas = repository.getAllReservas()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /**
     * Salva (cria ou atualiza) uma reserva.
     * Constrói o objeto Reserva antes de chamar o Repositório.
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
            // 1. CONSTRÓI O OBJETO RESERVA AQUI
            val reserva = Reserva(
                id = id ?: "",
                quartoId = quartoId,
                hospedeId = hospedeId,
                nomeCliente = nomeCliente,
                dataCheckIn = dataCheckIn,
                dataCheckOut = dataCheckOut,
                status = status
            )

            // 2. DELEGA A LÓGICA DE PERSISTÊNCIA AO REPOSITÓRIO
            if (id.isNullOrBlank()) {
                repository.addReserva(reserva)
            } else {
                repository.updateReserva(reserva)
            }

            // NOTA: A validação de conflito de datas (RF12) deve vir aqui antes de salvar
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

    // Factory
    class Factory(private val repository: ReservasRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ReservaViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ReservaViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}