package com.example.reservashotel.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reservashotel.data.model.Quarto
import com.example.reservashotel.data.repository.QuartosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class QuartoViewModel(private val repository: QuartosRepository) : ViewModel() {

    private val _listaQuartos = MutableStateFlow<List<Quarto>>(emptyList())
    val listaQuartos: StateFlow<List<Quarto>> = _listaQuartos

    init {
        // Agora carrega e observa o Flow corrigido
        carregarQuartos()
    }

    /**
     * Carrega todos os quartos observando o Flow do repositório
     */
    fun carregarQuartos() {
        viewModelScope.launch {
            // O QuartoRepository.getAllQuartos() agora retorna Flow, permitindo o collect
            repository.getAllQuartos().collect { quartos ->
                _listaQuartos.value = quartos
            }
        }
    }

    /**
     * Salva ou atualiza um quarto
     */
    fun salvarQuarto(
        id: String? = null,
        numero: String,
        tipo: String,
        valorDiaria: Double,
        status: String
    ) {
        viewModelScope.launch {
            // CORRIGIDO: O id é Int no modelo, deve ser convertido ou 0 se for novo.
            val quartoIdInt = id?.toIntOrNull() ?: 0

            val quarto = Quarto(
                id = quartoIdInt, // id é 0 para inserção ou o id existente para atualização
                numero = numero.toIntOrNull() ?: 0, // Garante que numero é Int
                tipo = tipo,
                valorDiaria = valorDiaria,
                status = status
            )

            // CORRIGIDO: Chama a nova assinatura que recebe o objeto Quarto
            repository.addOrUpdateQuarto(quarto)
        }
    }

    /**
     * Exclui um quarto
     */
    fun excluirQuarto(quarto: Quarto) {
        viewModelScope.launch {
            // CORRIGIDO: Renomeado para a função correta do Repository
            repository.excluirQuarto(quarto)
        }
    }

    /**
     * Factory para usar com ViewModelProvider
     */
    class Factory(private val repository: QuartosRepository) :
        androidx.lifecycle.ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(QuartoViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                // ATENÇÃO: Se estiver usando ReservasRepository na MainActivity,
                // você pode precisar renomear o tipo do parâmetro da Factory.
                return QuartoViewModel(repository as QuartosRepository) as T
            }
            throw IllegalArgumentException("Classe ViewModel desconhecida")
        }
    }
}