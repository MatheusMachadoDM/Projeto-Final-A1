package com.example.reservashotel.data.repository

import com.example.reservashotel.data.dao.UsuarioDao
import com.example.reservashotel.data.model.Usuario
import javax.inject.Inject


class UsuariosRepository @Inject constructor(
    private val usuarioDao: UsuarioDao
) {
    suspend fun addUsuario(usuario: Usuario) {
        usuarioDao.insertUsuario(usuario)
    }

    suspend fun getUsuarioByEmail(email: String): Usuario? {
        return usuarioDao.getUsuarioByEmail(email)
    }

    suspend fun autenticarUsuario(email: String, senha: String): Usuario? {
        return usuarioDao.autenticarUsuario(email, senha)
    }
}