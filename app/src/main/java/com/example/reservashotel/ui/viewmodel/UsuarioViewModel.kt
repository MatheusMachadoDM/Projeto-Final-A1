package com.example.reservashotel.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.reservashotel.data.model.Usuario
import com.example.reservashotel.data.repository.UsuariosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UsuarioViewModel(
    private val usuariosRepository: UsuariosRepository
) : ViewModel() {


    private val _loginSucesso = MutableStateFlow(false)
    val loginSucesso: StateFlow<Boolean> = _loginSucesso

    private val _mensagemErro = MutableStateFlow<String?>(null)
    val mensagemErro: StateFlow<String?> = _mensagemErro

    init {
        // Chamamos a função ao criar o ViewModel para garantir que o usuário de teste exista
        adicionarNovoUsuarioDeTeste()
    }

    /**
     * Tenta autenticar o usuário com o email e a senha fornecidos.
     */
    fun realizarLogin(email: String, senha: String) {
        // Reseta o estado de login
        _loginSucesso.value = false
        // _mensagemErro.value = null

        viewModelScope.launch {
            val usuario = usuariosRepository.autenticarUsuario(email, senha)

            if (usuario != null) {
                // Login bem-sucedido
                _loginSucesso.value = true
            } else {
                _mensagemErro.value = "Email ou senha incorretos. Tente novamente."
            }
        }
    }


    fun adicionarNovoUsuarioDeTeste() {
        viewModelScope.launch {
            val emailDeTeste = "admin@hotel.com"

            // Verifica se o usuário já existe
            val usuarioExistente = usuariosRepository.getUsuarioByEmail(emailDeTeste)

            if (usuarioExistente == null) {
                // Só insere se o usuário não for encontrado
                val novoUsuario = Usuario(
                    nomeUsuario = "admin",
                    email = emailDeTeste,
                    senha = "senha123"
                )
                usuariosRepository.addUsuario(novoUsuario)
            }
        }
    }

    class Factory(
        // A Factory recebe o repositório que o ViewModel precisa
        private val usuariosRepository: UsuariosRepository
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            // Verifica se a classe pedida é a UsuarioViewModel
            if (modelClass.isAssignableFrom(UsuarioViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                // Retorna uma nova instância de UsuarioViewModel, passando o repositório
                return UsuarioViewModel(usuariosRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}