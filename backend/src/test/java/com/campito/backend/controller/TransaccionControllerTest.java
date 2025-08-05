package com.campito.backend.controller;

import com.campito.backend.dto.*;
import com.campito.backend.model.TipoTransaccion;
import com.campito.backend.service.TransaccionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransaccionController.class)
public class TransaccionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransaccionService transaccionService;

    @Autowired
    private ObjectMapper objectMapper;

    // Tests para registrarTransaccion

    @Test
    @WithMockUser(username = "user")
    void registrarTransaccion_cuandoExitoso_entoncesStatus201() throws Exception {
        TransaccionDTO transaccionDTO = new TransaccionDTO(null, LocalDate.now(), 100.0f, TipoTransaccion.INGRESO, "Test Desc", "User Test", 1L, 1L, null, null);
        when(transaccionService.registrarTransaccion(any(TransaccionDTO.class))).thenReturn(transaccionDTO);

        mockMvc.perform(post("/transaccion/registrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transaccionDTO))
                .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.monto").value(100.0f));
    }

    @Test
    @WithMockUser(username = "user")
    void registrarTransaccion_cuandoDatosInvalidos_entoncesStatus400() throws Exception {
        TransaccionDTO transaccionDTO = new TransaccionDTO(null, null, null, null, "", "", null, null, null, null); // Datos inválidos

        mockMvc.perform(post("/transaccion/registrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transaccionDTO))
                .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "user")
    void registrarTransaccion_cuandoErrorInterno_entoncesStatus500() throws Exception {
        TransaccionDTO transaccionDTO = new TransaccionDTO(null, LocalDate.now(), 100.0f, TipoTransaccion.INGRESO, "Test Desc", "User Test", 1L, 1L, null, null);
        doThrow(new RuntimeException("Error inesperado")).when(transaccionService).registrarTransaccion(any(TransaccionDTO.class));

        mockMvc.perform(post("/transaccion/registrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transaccionDTO))
                .with(csrf()))
                .andExpect(status().isInternalServerError());
    }

    // Tests para removerTransaccion

    @Test
    @WithMockUser(username = "user")
    void removerTransaccion_cuandoExitoso_entoncesStatus200() throws Exception {
        doNothing().when(transaccionService).removerTransaccion(anyLong());

        mockMvc.perform(delete("/transaccion/remover/1").with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user")
    void removerTransaccion_cuandoNoEncontrado_entoncesStatus404() throws Exception {
        doThrow(new jakarta.persistence.EntityNotFoundException("Transacción no encontrada")).when(transaccionService).removerTransaccion(anyLong());

        mockMvc.perform(delete("/transaccion/remover/999").with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user")
    void removerTransaccion_cuandoErrorInterno_entoncesStatus500() throws Exception {
        doThrow(new RuntimeException("Error de base de datos")).when(transaccionService).removerTransaccion(anyLong());

        mockMvc.perform(delete("/transaccion/remover/1").with(csrf()))
                .andExpect(status().isInternalServerError());
    }

    // Tests para buscarTransaccion

    @Test
    @WithMockUser(username = "user")
    void buscarTransaccion_cuandoExitoso_entoncesStatus200() throws Exception {
        TransaccionBusquedaDTO busquedaDTO = new TransaccionBusquedaDTO(7, 2025, null, null, 1L);
        List<TransaccionListadoDTO> listado = Collections.singletonList(new TransaccionListadoDTO(1L, LocalDate.now(), 50.0f, TipoTransaccion.GASTO, "Compra", "User", LocalDateTime.now(), 1L, "Espacio", 1L, "Motivo", null, null, null));
        when(transaccionService.buscarTransaccion(any(TransaccionBusquedaDTO.class))).thenReturn(listado);

        mockMvc.perform(post("/transaccion/buscar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(busquedaDTO))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].descripcion").value("Compra"));
    }

    @Test
    @WithMockUser(username = "user")
    void buscarTransaccion_cuandoDatosInvalidos_entoncesStatus400() throws Exception {
        TransaccionBusquedaDTO busquedaDTO = new TransaccionBusquedaDTO(null, null, null, null, null); // Datos inválidos

        mockMvc.perform(post("/transaccion/buscar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(busquedaDTO))
                .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "user")
    void buscarTransaccion_cuandoErrorInterno_entoncesStatus500() throws Exception {
        TransaccionBusquedaDTO busquedaDTO = new TransaccionBusquedaDTO(7, 2025, null, null, 1L);
        doThrow(new RuntimeException("Error de servicio")).when(transaccionService).buscarTransaccion(any(TransaccionBusquedaDTO.class));

        mockMvc.perform(post("/transaccion/buscar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(busquedaDTO))
                .with(csrf()))
                .andExpect(status().isInternalServerError());
    }

    // Tests para registrarContactoTransferencia

    @Test
    @WithMockUser(username = "user")
    void registrarContactoTransferencia_cuandoExitoso_entoncesStatus201() throws Exception {
        ContactoDTO contactoDTO = new ContactoDTO(null, "Nuevo Contacto", 1L);
        when(transaccionService.registrarContactoTransferencia(any(ContactoDTO.class))).thenReturn(contactoDTO);

        mockMvc.perform(post("/transaccion/contacto/registrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(contactoDTO))
                .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Nuevo Contacto"));
    }

    @Test
    @WithMockUser(username = "user")
    void registrarContactoTransferencia_cuandoDatosInvalidos_entoncesStatus400() throws Exception {
        ContactoDTO contactoDTO = new ContactoDTO(null, "", null); // Datos inválidos

        mockMvc.perform(post("/transaccion/contacto/registrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(contactoDTO))
                .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "user")
    void registrarContactoTransferencia_cuandoErrorInterno_entoncesStatus500() throws Exception {
        ContactoDTO contactoDTO = new ContactoDTO(null, "Nuevo Contacto", 1L);
        doThrow(new RuntimeException("Error de servicio")).when(transaccionService).registrarContactoTransferencia(any(ContactoDTO.class));

        mockMvc.perform(post("/transaccion/contacto/registrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(contactoDTO))
                .with(csrf()))
                .andExpect(status().isInternalServerError());
    }

    // Tests para listarContactos

    @Test
    @WithMockUser(username = "user")
    void listarContactos_cuandoExitoso_entoncesStatus200() throws Exception {
        List<ContactoListadoDTO> listado = Collections.singletonList(new ContactoListadoDTO(1L, "Contacto Existente"));
        when(transaccionService.listarContactos(anyLong())).thenReturn(listado);

        mockMvc.perform(get("/transaccion/contacto/listar/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Contacto Existente"));
    }

    @Test
    @WithMockUser(username = "user")
    void listarContactos_cuandoNoEncontrado_entoncesStatus404() throws Exception {
        doThrow(new jakarta.persistence.EntityNotFoundException("Espacio de trabajo no encontrado")).when(transaccionService).listarContactos(anyLong());

        mockMvc.perform(get("/transaccion/contacto/listar/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user")
    void listarContactos_cuandoErrorInterno_entoncesStatus500() throws Exception {
        doThrow(new RuntimeException("Error de base de datos")).when(transaccionService).listarContactos(anyLong());

        mockMvc.perform(get("/transaccion/contacto/listar/1"))
                .andExpect(status().isInternalServerError());
    }

    // Tests para nuevoMotivoTransaccion

    @Test
    @WithMockUser(username = "user")
    void nuevoMotivoTransaccion_cuandoExitoso_entoncesStatus201() throws Exception {
        MotivoDTO motivoDTO = new MotivoDTO(null, "Nuevo Motivo", 1L);
        when(transaccionService.nuevoMotivoTransaccion(any(MotivoDTO.class))).thenReturn(motivoDTO);

        mockMvc.perform(post("/transaccion/motivo/registrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(motivoDTO))
                .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.motivo").value("Nuevo Motivo"));
    }

    @Test
    @WithMockUser(username = "user")
    void nuevoMotivoTransaccion_cuandoDatosInvalidos_entoncesStatus400() throws Exception {
        MotivoDTO motivoDTO = new MotivoDTO(null, "", null); // Datos inválidos

        mockMvc.perform(post("/transaccion/motivo/registrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(motivoDTO))
                .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "user")
    void nuevoMotivoTransaccion_cuandoErrorInterno_entoncesStatus500() throws Exception {
        MotivoDTO motivoDTO = new MotivoDTO(null, "Nuevo Motivo", 1L);
        doThrow(new RuntimeException("Error de servicio")).when(transaccionService).nuevoMotivoTransaccion(any(MotivoDTO.class));

        mockMvc.perform(post("/transaccion/motivo/registrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(motivoDTO))
                .with(csrf()))
                .andExpect(status().isInternalServerError());
    }

    // Tests para listarMotivos

    @Test
    @WithMockUser(username = "user")
    void listarMotivos_cuandoExitoso_entoncesStatus200() throws Exception {
        List<MotivoListadoDTO> listado = Collections.singletonList(new MotivoListadoDTO(1L, "Motivo Existente"));
        when(transaccionService.listarMotivos(anyLong())).thenReturn(listado);

        mockMvc.perform(get("/transaccion/motivo/listar/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].motivo").value("Motivo Existente"));
    }

    @Test
    @WithMockUser(username = "user")
    void listarMotivos_cuandoNoEncontrado_entoncesStatus404() throws Exception {
        doThrow(new jakarta.persistence.EntityNotFoundException("Espacio de trabajo no encontrado")).when(transaccionService).listarMotivos(anyLong());

        mockMvc.perform(get("/transaccion/motivo/listar/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user")
    void listarMotivos_cuandoErrorInterno_entoncesStatus500() throws Exception {
        doThrow(new RuntimeException("Error de base de datos")).when(transaccionService).listarMotivos(anyLong());

        mockMvc.perform(get("/transaccion/motivo/listar/1"))
                .andExpect(status().isInternalServerError());
    }

    // Tests para buscarTransaccionesRecientes

    @Test
    @WithMockUser(username = "user")
    void buscarTransaccionesRecientes_cuandoExitoso_entoncesStatus200() throws Exception {
        List<TransaccionListadoDTO> listado = Collections.singletonList(new TransaccionListadoDTO(1L, LocalDate.now(), 200.0f, TipoTransaccion.GASTO, "Reciente", "User", LocalDateTime.now(), 1L, "Espacio", 1L, "Motivo", null, null,null));
        when(transaccionService.buscarTransaccionesRecientes(anyLong())).thenReturn(listado);

        mockMvc.perform(get("/transaccion/buscarRecientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].descripcion").value("Reciente"));
    }

    @Test
    @WithMockUser(username = "user")
    void buscarTransaccionesRecientes_cuandoNoEncontrado_entoncesStatus404() throws Exception {
        doThrow(new jakarta.persistence.EntityNotFoundException("Espacio de trabajo no encontrado")).when(transaccionService).buscarTransaccionesRecientes(anyLong());

        mockMvc.perform(get("/transaccion/buscarRecientes/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user")
    void buscarTransaccionesRecientes_cuandoErrorInterno_entoncesStatus500() throws Exception {
        doThrow(new RuntimeException("Error de base de datos")).when(transaccionService).buscarTransaccionesRecientes(anyLong());

        mockMvc.perform(get("/transaccion/buscarRecientes/1"))
                .andExpect(status().isInternalServerError());
    }
}