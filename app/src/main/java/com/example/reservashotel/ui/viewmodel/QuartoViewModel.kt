package com.example.reservashotel.ui.viewmodel

import androidx.lifecycle.ViewModel // Classe base do Androidx ViewModel.
import androidx.lifecycle.viewModelScope // Extensão para coroutines ligadas ao ciclo de vida do ViewModel.
import com.example.reservashotel.data.model.Quarto // Modelo de dados da entidade.
import com.example.reservashotel.data.repository.QuartosRepository // Repositório da camada de dados.
import kotlinx.coroutines.flow.MutableStateFlow // Implementação mutável do Flow.
import kotlinx.coroutines.flow.StateFlow // Versão imutável do Flow (melhor para expor o estado).
import kotlinx.coroutines.launch // Inicia uma coroutine.

/**
 * ViewModel responsável por gerenciar e expor os dados da entidade Quarto para a UI.
 * Implementa a lógica de negócios e se comunica com o Repositório.
 *
 * @param repository O Repositório de Quartos injetado para acesso aos dados.
 */
class QuartoViewModel(private val repository: QuartosRepository) : ViewModel() {

    // --- Gerenciamento de Estado Reativo ---

    // Estado interno (mutável) da lista de quartos. Atualizado pelo ViewModel.
    private val _listaQuartos = MutableStateFlow<List<Quarto>>(emptyList())

    // Estado externo (imutável) que a UI observa (StateFlow).
    val listaQuartos: StateFlow<List<Quarto>> = _listaQuartos

    /**
     * Bloco de inicialização. Chamado assim que a instância do ViewModel é criada.
     */
    init {
        // Inicia o carregamento dos quartos para preencher a lista inicial.
        carregarQuartos()
    }

    /**
     * Carrega todos os quartos observando o Flow do repositório.
     */
    fun carregarQuartos() {
        // Inicia uma coroutine no escopo do ViewModel.
        viewModelScope.launch {
            // O QuartoRepository.getAllQuartos() retorna Flow (reativo), permitindo o 'collect'.
            // 'collect' recebe cada nova lista emitida pelo Room/Repository.
            repository.getAllQuartos().collect { quartos ->
                _listaQuartos.value = quartos // Atualiza o estado reativo (_listaQuartos).
            }
        }
    }

    /**
     * Salva (cria) ou atualiza um quarto existente.
     * Recebe os campos do formulário (UI).
     */
    fun salvarQuarto(
        id: String? = null,
        numero: String,
        tipo: String,
        valorDiaria: Double,
        status: String
    ) {
        viewModelScope.launch {
            // CORRIGIDO: O ID do modelo Quarto é Int. Converte o ID String (da navegação) para Int.
            // Se o ID for nulo (novo quarto) ou falhar na conversão, usa 0 (que ativa o autoGenerate no Room).
            val quartoIdInt = id?.toIntOrNull() ?: 0

            // Constrói o objeto Quarto a ser salvo.
            val quarto = Quarto(
                id = quartoIdInt, // 0 para inserção ou ID existente para atualização.
                numero = numero.toIntOrNull() ?: 0, // Converte String (do TextField) para Int.
                tipo = tipo,
                valorDiaria = valorDiaria,
                status = status
            )

            // Delega a operação de persistência ao Repositório.
            repository.addOrUpdateQuarto(quarto)
        }
    }

    /**
     * Exclui um quarto.
     */
    fun excluirQuarto(quarto: Quarto) {
        viewModelScope.launch {
            // Chama a função de exclusão no Repositório.
            repository.excluirQuarto(quarto)
        }
    }

    /**
     * Carrega um quarto específico pelo seu ID. Usado principalmente para preencher
     * o formulário na tela de edição (FormQuartoScreen).
     *
     * @param id ID do quarto (como String, vindo da navegação).
     * @return O objeto Quarto ou null.
     */
    suspend fun carregarQuartoPorId(id: String): Quarto? {
        return repository.getQuartoById(id)
    }

    /**
     * Factory (Fábrica) para criar instâncias do QuartoViewModel com dependências.
     * Essencial para injetar o Repositório no ViewModel, pois ele possui um construtor com parâmetros.
     */
    class Factory(private val repository: QuartosRepository) :
        androidx.lifecycle.ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            // Verifica se a classe pedida é a QuartoViewModel.
            if (modelClass.isAssignableFrom(QuartoViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                // Cria e retorna a instância do ViewModel, passando o Repositório.
                return QuartoViewModel(repository as QuartosRepository) as T
            }
            throw IllegalArgumentException("Classe ViewModel desconhecida")
        }
    }
}