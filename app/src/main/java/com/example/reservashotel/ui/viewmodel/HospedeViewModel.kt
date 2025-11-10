package com.example.reservashotel.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reservashotel.data.model.Hospede
import com.example.reservashotel.data.repository.HospedesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HospedeViewModel(private val repository: HospedesRepository) : ViewModel() {

    private val _listaHospedes = MutableStateFlow<List<Hospede>>(emptyList())
    val listaHospedes: StateFlow<List<Hospede>> = _listaHospedes

    init {
        carregarHospedes()
    }

    fun carregarHospedes() {
        viewModelScope.launch {
            repository.getAllHospedes().collect { hospedes ->
                _listaHospedes.value = hospedes
            }
        }
    }

    // 🌟 NOVO: Função para carregar um hóspede por ID
    suspend fun carregarHospedePorId(id: String): Hospede? {
        // Converte o ID de String (navegação) para Int
        val idInt = id.toIntOrNull() ?: return null

        // Chama o Repositório com o tipo correto
        return repository.getHospedeById(idInt)
    }

    fun salvarHospede(
        id: String? = null, // Recebido como String da navegação
        nome: String,
        cpf: String,
        email: String,
        telefone: String
    ) {
        viewModelScope.launch {
            // Converte String? para Int. Usa 0 para autoincrementar se for novo
            val hospedeIdInt = id?.toIntOrNull() ?: 0

            val hospede = Hospede(
                id = hospedeIdInt,
                nome = nome,
                cpf = cpf,
                email = email,
                telefone = telefone
            )

            repository.addOrUpdateHospede(hospede)
        }
    }

    fun excluirHospede(hospede: Hospede) {
        viewModelScope.launch {
            repository.excluirHospede(hospede)
        }
    }

    class Factory(private val repository: HospedesRepository) :
        androidx.lifecycle.ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HospedeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HospedeViewModel(repository) as T
            }
            throw IllegalArgumentException("Classe ViewModel desconhecida")
        }
    }
}