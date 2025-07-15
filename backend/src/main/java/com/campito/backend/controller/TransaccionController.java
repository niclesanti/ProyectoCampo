package com.campito.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.campito.backend.dto.ContactoDTO;
import com.campito.backend.dto.ContactoListadoDTO;
import com.campito.backend.dto.MotivoDTO;
import com.campito.backend.dto.MotivoListadoDTO;
import com.campito.backend.dto.TransaccionBusquedaDTO;
import com.campito.backend.dto.TransaccionDTO;
import com.campito.backend.dto.TransaccionListadoDTO;
import com.campito.backend.service.TransaccionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;



@RestController
@RequestMapping("/transaccion")
@Tag(name = "Transaccion", description = "Operaciones para la gestión de transacciones")
public class TransaccionController {

    private final TransaccionService transaccionService;

    @Autowired
    public TransaccionController(TransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }

    @Operation(summary = "Registrar una nueva transacción",
                description = "Permite registrar una nueva transacción en el sistema.",
                responses = {
                    @ApiResponse(responseCode = "201", description = "Transacción registrada correctamente"),
                    @ApiResponse(responseCode = "400", description = "Error al registrar la transacción"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
                })
    @PostMapping("/registrar")
    public ResponseEntity<Void> registrarTransaccion(@Valid @RequestBody TransaccionDTO transaccionDTO) {
        transaccionService.registrarTransaccion(transaccionDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Remover transacción",
                description = "Permite remover una transacción existente en el sistema.",
                responses = {
                    @ApiResponse(responseCode = "200", description = "Transacción removida correctamente"),
                    @ApiResponse(responseCode = "404", description = "Transacción no encontrada"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
                })
    @DeleteMapping("/remover/{id}")
    public ResponseEntity<Void> removerTransaccion(@PathVariable Long id) {
        transaccionService.removerTransaccion(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Buscar transacciones",
                description = "Permite buscar transacciones según criterios específicos.",
                responses = {
                    @ApiResponse(responseCode = "200", description = "Transacciones encontradas"),
                    @ApiResponse(responseCode = "400", description = "Error en los criterios de búsqueda"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
                })
    @PostMapping("/buscar")
    public ResponseEntity<List<TransaccionListadoDTO>> buscarTransaccion(@Valid @RequestBody TransaccionBusquedaDTO datosBusqueda) {
        List<TransaccionListadoDTO> transacciones = transaccionService.buscarTransaccion(datosBusqueda);
        return new ResponseEntity<>(transacciones, HttpStatus.OK);
    }

    @Operation(summary = "Registrar contacto de transferencia",
                description = "Permite registrar un nuevo contacto de transferencia.",
                responses = {
                    @ApiResponse(responseCode = "201", description = "Contacto registrado correctamente"),
                    @ApiResponse(responseCode = "400", description = "Error al registrar el contacto"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
                })
    @PostMapping("/contacto/registrar")
    public ResponseEntity<Void> registrarContactoTransferencia(@Valid @RequestBody ContactoDTO contactoDTO) {
        transaccionService.registrarContactoTransferencia(contactoDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Listar contactos de transferencia por espacio de trabajo",
                description = "Permite listar los contactos de transferencia asociados a un espacio de trabajo.",
                responses = {
                    @ApiResponse(responseCode = "200", description = "Contactos listados correctamente"),
                    @ApiResponse(responseCode = "404", description = "Espacio de trabajo no encontrado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
                })
    @GetMapping("/contacto/listar/{idEspacioTrabajo}")
    public ResponseEntity<List<ContactoListadoDTO>> listarContactos(@PathVariable Long idEspacioTrabajo) {
        List<ContactoListadoDTO> contactos = transaccionService.listarContactos(idEspacioTrabajo);
        return new ResponseEntity<>(contactos, HttpStatus.OK);
    }

    @Operation(summary = "Registrar motivo de transacción",
                description = "Permite registrar un nuevo motivo de transacción.",
                responses = {
                    @ApiResponse(responseCode = "201", description = "Motivo registrado correctamente"),
                    @ApiResponse(responseCode = "400", description = "Error al registrar el motivo"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
                })
    @PostMapping("/motivo/registrar")
    public ResponseEntity<MotivoDTO> nuevoMotivoTransaccion(@Valid @RequestBody MotivoDTO motivoDTO) {
        MotivoDTO nuevoMotivo = transaccionService.nuevoMotivoTransaccion(motivoDTO);
        return new ResponseEntity<>(nuevoMotivo, HttpStatus.CREATED);
    }

    @Operation(summary = "Listar motivos de transacción por espacio de trabajo",
                description = "Permite listar los motivos de transacción asociados a un espacio de trabajo.",
                responses = {
                    @ApiResponse(responseCode = "200", description = "Motivos listados correctamente"),
                    @ApiResponse(responseCode = "404", description = "Espacio de trabajo no encontrado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
                })
    @GetMapping("/motivo/listar/{idEspacioTrabajo}")
    public ResponseEntity<List<MotivoListadoDTO>> listarMotivos(@PathVariable Long idEspacioTrabajo) {
        List<MotivoListadoDTO> motivos = transaccionService.listarMotivos(idEspacioTrabajo);
        return new ResponseEntity<>(motivos, HttpStatus.OK);
    }
}
