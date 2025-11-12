package com.example.reservashotel.data.repository

import com.example.reservashotel.data.dao.QuartoDao // Importa a interface DAO para acesso ao banco de dados Room
import com.example.reservashotel.data.model.Quarto // Importa o modelo de dados Quarto
import kotlinx.coroutines.flow.Flow // Importa Flow para trabalhar com dados reativos

/**
 * Repository (Repositório) para a entidade Quarto.
 * Esta classe é responsável por abstrair as fontes de dados (neste caso, o Room via QuartoDao)
 * da camada ViewModel.
 *
 * @param quartoDao O Data Access Object injetado via construtor (Injeção de Dependência).
 */
class QuartosRepository(private val quartoDao: QuartoDao) {

    /**
     * Obtém a lista de todos os quartos do banco de dados local (Room).
     * O retorno é um Flow, que permite que o ViewModel observe as mudanças em tempo real.
     */
    fun getAllQuartos(): Flow<List<Quarto>> = quartoDao.getAll()

    /**
     * Adiciona um novo quarto ou atualiza um quarto existente.
     * O DAO utiliza OnConflictStrategy.REPLACE (definido no DAO) para gerenciar esta lógica.
     *
     * @param quarto O objeto Quarto a ser inserido ou atualizado.
     */
    suspend fun addOrUpdateQuarto(quarto: Quarto) {
        quartoDao.insert(quarto)
    }

    /**
     * Exclui um quarto do banco de dados local.
     *
     * @param quarto O objeto Quarto a ser excluído.
     */
    suspend fun excluirQuarto(quarto: Quarto) {
        quartoDao.delete(quarto)
    }

    /**
     * Busca um quarto específico pelo seu ID.
     * Usado principalmente para carregar dados em telas de edição.
     *
     * @param id O ID do quarto (recebido como String da navegação/UI).
     * @return O objeto Quarto correspondente ou null.
     */
    suspend fun getQuartoById(id: String): Quarto? {
        // Chama a função correspondente no DAO.
        // O QuartoViewModel garante que este ID será convertido ou tratado.
        return quartoDao.getQuartoById(id)
    }
}