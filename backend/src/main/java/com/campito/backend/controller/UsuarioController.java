package com.campito.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.campito.backend.dto.UsuarioDTO;
import com.campito.backend.model.CustomUserDetails;
import com.campito.backend.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("")
@Tag(name = "Usuario", description = "Operaciones para la gestión de usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Obtener datos del usuario autenticado", description = "Devuelve el id, nombre y email del usuario que ha iniciado sesión.")
    @ApiResponse(responseCode = "200", description = "Datos del usuario")
    @ApiResponse(responseCode = "401", description = "No autorizado")
    @GetMapping("/usuario/me")
    public UsuarioDTO getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            return null; // O lanzar una excepción de no autorizado
        }
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return new UsuarioDTO(userDetails.getId(), userDetails.getNombre(), userDetails.getUsername(), null);
    }

    @Operation(summary = "Registrar usuario manualmente", description = "Permite registrar un usuario con email y clave.")
    @ApiResponse(responseCode = "201", description = "Usuario registrado correctamente")
    @ApiResponse(responseCode = "400", description = "Solicitud incorrecta")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    @PostMapping("/usuario/registrar")
    public void registrarUsuarioManualmente(@RequestBody UsuarioDTO usuarioDTO) {
        usuarioService.registrarUsuarioManualmente(usuarioDTO);
    }
}
