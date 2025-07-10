package com.campito.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;

import com.campito.backend.dto.UsuarioDTO;
import com.campito.backend.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("")
@Tag(name = "Usuario", description = "Operaciones para la gestión de usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Registrar usuario manualmente", description = "Permite registrar un usuario con email y clave.")
    @ApiResponse(responseCode = "201", description = "Usuario registrado correctamente")
    @ApiResponse(responseCode = "400", description = "Solicitud incorrecta")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    @PostMapping("/usuario/registrar")
    public void registrarUsuarioManualmente(@RequestBody UsuarioDTO usuarioDTO) {
        usuarioService.registrarUsuarioManualmente(usuarioDTO);
    }

    @Operation(summary = "Iniciar sesión manualmente", description = "Permite iniciar sesión con email y clave.")
    @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso")
    @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    @PostMapping("/usuario/login")
    @ResponseBody
    public boolean iniciarSesion(@RequestBody UsuarioDTO usuarioDTO) {
        return usuarioService.iniciarSesion(usuarioDTO);
    }
}
