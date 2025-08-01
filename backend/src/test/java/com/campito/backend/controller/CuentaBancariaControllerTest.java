package com.campito.backend.controller;

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

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.campito.backend.dto.CuentaBancariaDTO;
import com.campito.backend.dto.CuentaBancariaListadoDTO;
import com.campito.backend.service.CuentaBancariaService;
import com.campito.backend.exception.ControllerAdvisor;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(CuentaBancariaController.class)
@Import(ControllerAdvisor.class)
public class CuentaBancariaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CuentaBancariaService cuentaBancariaService;

    @Autowired
    private ObjectMapper objectMapper;

    // Tests para crearCuentaBancaria
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCrearCuentaBancaria_conErrorInterno_retorna500() throws Exception {
        CuentaBancariaDTO dto = new CuentaBancariaDTO(null, "Nombre", "Entidad", 1L);
        doThrow(new RuntimeException("Error inesperado")).when(cuentaBancariaService).crearCuentaBancaria(any(CuentaBancariaDTO.class));

        mockMvc.perform(post("/cuentabancaria/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(csrf()))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCrearCuentaBancaria_conDatosInvalidos_retorna400() throws Exception {
        CuentaBancariaDTO dto = new CuentaBancariaDTO(null, "", "", null);// Datos inválidos
        mockMvc.perform(post("/cuentabancaria/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCrearCuentaBancaria_conDatosValidos_retorna201() throws Exception {
        CuentaBancariaDTO dto = new CuentaBancariaDTO(null, "Nombre", "Entidad", 1L);
        doNothing().when(cuentaBancariaService).crearCuentaBancaria(any(CuentaBancariaDTO.class));

        mockMvc.perform(post("/cuentabancaria/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(csrf()))
                .andExpect(status().isCreated());
    }

    // Tests para listarCuentasBancarias
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testListarCuentasBancarias_conErrorInterno_retorna500() throws Exception {
        // Arrange
        when(cuentaBancariaService.listarCuentasBancarias(anyLong())).thenThrow(new RuntimeException("Error inesperado"));

        // Act & Assert
        mockMvc.perform(get("/cuentabancaria/listar/1"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testListarCuentasBancarias_conIdInvalido_retorna400() throws Exception {
        when(cuentaBancariaService.listarCuentasBancarias(anyLong())).thenThrow(new IllegalArgumentException("Id inválido"));
        mockMvc.perform(get("/cuentabancaria/listar/999"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testListarCuentasBancarias_conDatosValidos_retorna200() throws Exception {
        List<CuentaBancariaListadoDTO> cuentas = Collections.singletonList(new CuentaBancariaListadoDTO(1L, "Nombre", "Entidad", 1000.0f));
        when(cuentaBancariaService.listarCuentasBancarias(1L)).thenReturn(cuentas);

        mockMvc.perform(get("/cuentabancaria/listar/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Nombre"))
                .andExpect(jsonPath("$[0].entidadFinanciera").value("Entidad"))
                .andExpect(jsonPath("$[0].saldoActual").value(1000.0f));
    }

    // Tests para realizarTransaccion
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testRealizarTransaccion_conErrorInterno_retorna500() throws Exception {
        doThrow(new RuntimeException("Error inesperado")).when(cuentaBancariaService).transaccionEntreCuentas(anyLong(), anyLong(), any(Float.class));

        mockMvc.perform(put("/cuentabancaria/transaccion/1/2/100.0")
                .with(csrf()))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testRealizarTransaccion_conDatosInvalidos_retorna400() throws Exception {
        doThrow(new IllegalArgumentException("Cuenta bancaria origen no encontrada")).when(cuentaBancariaService).transaccionEntreCuentas(1L, 2L, 1000.0f);

        mockMvc.perform(put("/cuentabancaria/transaccion/1/2/1000.0")
                .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testRealizarTransaccion_conDatosValidos_retorna200() throws Exception {
        doNothing().when(cuentaBancariaService).transaccionEntreCuentas(1L, 2L, 100.0f);

        mockMvc.perform(put("/cuentabancaria/transaccion/1/2/100.0")
                .with(csrf()))
                .andExpect(status().isOk());
    }
}
