package com.campito.backend.controller;

import com.campito.backend.dto.EspacioTrabajoDTO;
import com.campito.backend.dto.EspacioTrabajoListadoDTO;
import com.campito.backend.service.EspacioTrabajoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EspacioTrabajoController.class)
public class EspacioTrabajoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EspacioTrabajoService espacioTrabajoService;

    @Autowired
    private ObjectMapper objectMapper;

    // Tests para registrarEspacioTrabajo

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void registrarEspacioTrabajo_cuandoExitoso_entoncesStatus201() throws Exception {
        EspacioTrabajoDTO dto = new EspacioTrabajoDTO("Mi Espacio", 1L);
        doNothing().when(espacioTrabajoService).registrarEspacioTrabajo(any(EspacioTrabajoDTO.class));

        mockMvc.perform(post("/espaciotrabajo/registrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(csrf()))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void registrarEspacioTrabajo_cuandoDatosInvalidos_entoncesStatus400() throws Exception {
        EspacioTrabajoDTO dto = new EspacioTrabajoDTO("", null); // Datos inválidos

        mockMvc.perform(post("/espaciotrabajo/registrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void registrarEspacioTrabajo_cuandoErrorInterno_entoncesStatus500() throws Exception {
        EspacioTrabajoDTO dto = new EspacioTrabajoDTO("Mi Espacio", 1L);
        doThrow(new RuntimeException("Error inesperado")).when(espacioTrabajoService).registrarEspacioTrabajo(any(EspacioTrabajoDTO.class));

        mockMvc.perform(post("/espaciotrabajo/registrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(csrf()))
                .andExpect(status().isInternalServerError());
    }

    // Tests para compartirEspacioTrabajo

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void compartirEspacioTrabajo_cuandoExitoso_entoncesStatus200() throws Exception {
        doNothing().when(espacioTrabajoService).compartirEspacioTrabajo("test@test.com", 1L, 1L);

        mockMvc.perform(put("/espaciotrabajo/compartir/test@test.com/1/1").with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void compartirEspacioTrabajo_cuandoErrorDeNegocio_entoncesStatus400() throws Exception {
        doThrow(new IllegalArgumentException("Usuario no encontrado")).when(espacioTrabajoService).compartirEspacioTrabajo("error@test.com", 1L, 1L);

        mockMvc.perform(put("/espaciotrabajo/compartir/error@test.com/1/1").with(csrf()))
                .andExpect(status().isBadRequest());
    }

    // Tests para listarEspaciosTrabajoPorUsuario

    @Test
    @WithMockUser(username = "user")
    void listarEspaciosTrabajoPorUsuario_cuandoExitoso_entoncesStatus200() throws Exception {
        List<EspacioTrabajoListadoDTO> listado = Collections.singletonList(new EspacioTrabajoListadoDTO(1L, "Mi Espacio", 1000.0f, 1L));
        when(espacioTrabajoService.listarEspaciosTrabajoPorUsuario(1L)).thenReturn(listado);

        mockMvc.perform(get("/espaciotrabajo/listar/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Mi Espacio"));
    }

    @Test
    @WithMockUser(username = "user")
    void listarEspaciosTrabajoPorUsuario_cuandoErrorDeNegocio_entoncesStatus400() throws Exception {
        when(espacioTrabajoService.listarEspaciosTrabajoPorUsuario(anyLong())).thenThrow(new IllegalArgumentException("ID de usuario inválido"));

        mockMvc.perform(get("/espaciotrabajo/listar/999"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "user")
    void listarEspaciosTrabajoPorUsuario_cuandoErrorInterno_entoncesStatus500() throws Exception {
        when(espacioTrabajoService.listarEspaciosTrabajoPorUsuario(anyLong())).thenThrow(new RuntimeException("Error de base de datos"));

        mockMvc.perform(get("/espaciotrabajo/listar/1"))
                .andExpect(status().isInternalServerError());
    }
}