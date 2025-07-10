package com.campito.backend.service;

import com.campito.backend.dto.UsuarioDTO;

public interface UsuarioService {
    public void registrarUsuarioManualmente(UsuarioDTO usuarioDTO);
    public boolean iniciarSesion(UsuarioDTO usuarioDTO);
}
