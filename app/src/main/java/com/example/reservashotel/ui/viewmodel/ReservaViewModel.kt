package com.example.reservashotel.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.reservashotel.data.model.Reserva
import com.example.reservashotel.data.repository.ReservasRepository
import com.example.reservashotel.data.repository.HospedesRepository
import com.example.reservashotel.data.repository.QuartosRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ReservaViewModel(
    private val repository: ReservasRepository,
    private val hospedesRepository: HospedesRepository,
    private val quartosRepository: QuartosRepository
) : ViewModel() {

    private val _mensagemErro = MutableStateFlow<String?>(null)
    val mensagemErro: StateFlow<String?> = _mensagemErro.asStateFlow()

    fun limparMensagemErro() {
        _mensagemErro.value = null
    }

    val listaReservas = repository.getAllReservas()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /**
     * Salva (cria ou atualiza) uma reserva com validações.
     */
    suspend fun salvarReserva(
        id: String? = null,
        quartoId: String,
        hospedeId: String,
        nomeCliente: String,
        dataCheckIn: Long,
        dataCheckOut: Long,
        status: String
    ) {
        // Limpa o erro anterior antes de uma nova tentativa
        _mensagemErro.value = null

        viewModelScope.launch {

            // 1. VALIDAÇÃO DE REGRA DE NEGÓCIO: Datas
            if (dataCheckOut <= dataCheckIn) {
                _mensagemErro.value = "A data de Check-out deve ser posterior à data de Check-in."
                return@launch
            }

            // 2. VALIDAÇÃO DE EXISTÊNCIA: Hóspede
            val nomeHospedeExistente = buscarNomeHospede(hospedeId)
            if (nomeHospedeExistente == null) {
                _mensagemErro.value = "Hóspede com ID '$hospedeId' não encontrado. Verifique o cadastro."
                return@launch
            }

            // 3. VALIDAÇÃO DE EXISTÊNCIA: Quarto
            val quarto = quartosRepository.getQuartoById(quartoId)
            if (quarto == null) {
                _mensagemErro.value = "Quarto com ID '$quartoId' não encontrado. Verifique o cadastro."
                return@launch
            }

            // 4. Se todas as validações passarem, prossegue com o salvamento
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

        _navegarDeVolta.emit(Unit)
    }

    fun excluirReserva(reserva: Reserva) {
        viewModelScope.launch {
            repository.deleteReserva(reserva)
        }
    }

    suspend fun carregarReservaPorId(id: String): Reserva? {
        return repository.getReservaById(id)
    }

    suspend fun buscarNomeHospede(id: String): String? {
        val idInt = id.toIntOrNull() ?: return null
        val hospede = hospedesRepository.getHospedeById(idInt)
        return hospede?.nome
    }

    class Factory(
        private val reservasRepository: ReservasRepository,
        private val hospedesRepository: HospedesRepository,
        private val quartosRepository: QuartosRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ReservaViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ReservaViewModel(reservasRepository, hospedesRepository, quartosRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    private val _navegarDeVolta = MutableSharedFlow<Unit>()
    val navegarDeVolta = _navegarDeVolta.asSharedFlow()
}