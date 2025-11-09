package com.example.reservashotel.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reservashotel.model.Reserva
import com.example.reservashotel.repository.ReservaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReservaViewModel(
    private val repository: ReservaRepository = ReservaRepository()
) : ViewModel() {

    private val _reservas = MutableStateFlow<List<Reserva>>(emptyList())
    val reservas: StateFlow<List<Reserva>> = _reservas

    private val _status = MutableStateFlow<String?>(null)
    val status: StateFlow<String?> = _status

    init {
        carregarReservas()
    }

    fun carregarReservas() {
        viewModelScope.launch {
            repository.listarReservas { lista ->
                _reservas.value = lista
            }
        }
    }

    fun salvarReserva(reserva: Reserva) {
        viewModelScope.launch {
            repository.salvarReserva(reserva) { sucesso ->
                if (sucesso) {
                    _status.value = "Reserva salva com sucesso!"
                    carregarReservas()
                } else {
                    _status.value = "Erro ao salvar reserva"
                }
            }
        }
    }

    fun excluirReserva(id: String) {
        viewModelScope.launch {
            repository.excluirReserva(id) { sucesso ->
                if (sucesso) {
                    _status.value = "Reserva excluída!"
                    carregarReservas()
                } else {
                    _status.value = "Erro ao excluir reserva"
                }
            }
        }
    }
}
