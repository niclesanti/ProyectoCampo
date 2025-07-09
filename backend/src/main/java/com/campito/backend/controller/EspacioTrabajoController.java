package com.campito.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.campito.backend.dto.EspacioTrabajoDTO;
import com.campito.backend.service.EspacioTrabajoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/espaciotrabajo")
@Tag(name = "EspacioTrabajo", description = "Operaciones para la gestión de espacios de trabajo")
public class EspacioTrabajoController {

    private final EspacioTrabajoService espacioTrabajoService;

    @Autowired
    public EspacioTrabajoController(EspacioTrabajoService espacioTrabajoService) {
        this.espacioTrabajoService = espacioTrabajoService;
    }

    @Operation(
        summary = "Registrar un nuevo espacio de trabajo",
        description = "Permite registrar un nuevo espacio de trabajo en el sistema."
    )
    @ApiResponse(responseCode = "201", description = "Espacio de trabajo registrado correctamente")
    @ApiResponse(responseCode = "400", description = "Error al registrar el espacio de trabajo")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    @PostMapping("/registrar")
    public ResponseEntity<Void> registrarEspacioTrabajo(@Valid @RequestBody EspacioTrabajoDTO espacioTrabajoDTO) {
        espacioTrabajoService.registrarEspacioTrabajo(espacioTrabajoDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
        summary = "Compartir espacio de trabajo",
        description = "Permite compartir un espacio de trabajo con un usuario."
    )
    @ApiResponse(responseCode = "200", description = "Espacio de trabajo compartido correctamente")
    @ApiResponse(responseCode = "400", description = "Error al compartir el espacio de trabajo")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    @PostMapping("/compartir/{idUsuario}/{idEspacioTrabajo}/{idUsuarioAdmin}")
    public ResponseEntity<Void> compartirEspacioTrabajo(
            @PathVariable Long idUsuario,
            @PathVariable Long idEspacioTrabajo,
            @PathVariable Long idUsuarioAdmin) {
        espacioTrabajoService.compartirEspacioTrabajo(idUsuario, idEspacioTrabajo, idUsuarioAdmin);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
