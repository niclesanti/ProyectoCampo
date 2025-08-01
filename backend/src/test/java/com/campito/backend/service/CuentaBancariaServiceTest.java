package com.campito.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.campito.backend.dao.CuentaBancariaRepository;
import com.campito.backend.dao.EspacioTrabajoRepository;
import com.campito.backend.dto.CuentaBancariaDTO;
import com.campito.backend.dto.CuentaBancariaListadoDTO;
import com.campito.backend.model.CuentaBancaria;
import com.campito.backend.model.EspacioTrabajo;
import com.campito.backend.model.ProveedorAutenticacion;
import com.campito.backend.model.TipoTransaccion;
import com.campito.backend.model.Usuario;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class CuentaBancariaServiceTest {

    @Mock
    private CuentaBancariaRepository cuentaBancariaRepository;

    @Mock
    private EspacioTrabajoRepository espacioTrabajoRepository;

    @InjectMocks
    private CuentaBancariaServiceImpl cuentaBancariaService;

    private CuentaBancariaDTO cuentaBancariaDTO;
    private EspacioTrabajo espacioTrabajo;
    private CuentaBancaria cuentaBancaria;

    @BeforeEach
    void setUp() {
        Usuario usuarioAdmin = new Usuario();
        usuarioAdmin.setEmail("admin@test.com");
        usuarioAdmin.setNombre("Admin User");
        usuarioAdmin.setProveedor(ProveedorAutenticacion.MANUAL);
        usuarioAdmin.setRol("ADMIN");
        usuarioAdmin.setActivo(true);
        usuarioAdmin.setFechaRegistro(LocalDateTime.now());

        espacioTrabajo = new EspacioTrabajo("Mi Espacio de Trabajo", 0f, usuarioAdmin);
        espacioTrabajo.setId(1L);

        cuentaBancariaDTO = new CuentaBancariaDTO(1L, "Cuenta de Ahorros", "Banco A", 1L);

        cuentaBancaria = new CuentaBancaria();
        cuentaBancaria.setId(1L);
        cuentaBancaria.setNombre("Cuenta de Ahorros");
        cuentaBancaria.setEntidadFinanciera("Banco A");
        cuentaBancaria.setSaldoActual(1000f);
        cuentaBancaria.setEspacioTrabajo(espacioTrabajo);
    }

    // Tests para crearCuentaBancaria
    @Test
    void testCrearCuentaBancaria_cuandoDTOEsNulo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> {
            cuentaBancariaService.crearCuentaBancaria(null);
        });
        verify(espacioTrabajoRepository, never()).findById(any());
        verify(cuentaBancariaRepository, never()).save(any());
    }

    @Test
    void testCrearCuentaBancaria_cuandoIdEspacioTrabajoEsNulo_lanzaExcepcion() {
        CuentaBancariaDTO dtoSinEspacio = new CuentaBancariaDTO(1L, "Nombre", "Entidad", null);
        assertThrows(IllegalArgumentException.class, () -> {
            cuentaBancariaService.crearCuentaBancaria(dtoSinEspacio);
        });
        verify(espacioTrabajoRepository, never()).findById(any());
        verify(cuentaBancariaRepository, never()).save(any());
    }

    @Test
    void testCrearCuentaBancaria_cuandoNombreEsNuloOVacio_lanzaExcepcion() {
        CuentaBancariaDTO dtoSinNombre = new CuentaBancariaDTO(1L, null, "Entidad", 1L);
        assertThrows(IllegalArgumentException.class, () -> {
            cuentaBancariaService.crearCuentaBancaria(dtoSinNombre);
        });

        CuentaBancariaDTO dtoNombreVacio = new CuentaBancariaDTO(1L, "", "Entidad", 1L);
        assertThrows(IllegalArgumentException.class, () -> {
            cuentaBancariaService.crearCuentaBancaria(dtoNombreVacio);
        });
        verify(espacioTrabajoRepository, never()).findById(any());
        verify(cuentaBancariaRepository, never()).save(any());
    }

    @Test
    void testCrearCuentaBancaria_cuandoEntidadFinancieraEsNulaOVacia_lanzaExcepcion() {
        CuentaBancariaDTO dtoSinEntidad = new CuentaBancariaDTO(1L, "Nombre", null, 1L);
        assertThrows(IllegalArgumentException.class, () -> {
            cuentaBancariaService.crearCuentaBancaria(dtoSinEntidad);
        });

        CuentaBancariaDTO dtoEntidadVacia = new CuentaBancariaDTO(1L, "Nombre", "", 1L);
        assertThrows(IllegalArgumentException.class, () -> {
            cuentaBancariaService.crearCuentaBancaria(dtoEntidadVacia);
        });
        verify(espacioTrabajoRepository, never()).findById(any());
        verify(cuentaBancariaRepository, never()).save(any());
    }

    @Test
    void testCrearCuentaBancaria_cuandoEspacioTrabajoNoExiste_lanzaExcepcion() {
        when(espacioTrabajoRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            cuentaBancariaService.crearCuentaBancaria(cuentaBancariaDTO);
        });
        verify(cuentaBancariaRepository, never()).save(any());
    }

    @Test
    void testCrearCuentaBancaria_conDatosValidos_guardaCuenta() {
        when(espacioTrabajoRepository.findById(1L)).thenReturn(Optional.of(espacioTrabajo));
        cuentaBancariaService.crearCuentaBancaria(cuentaBancariaDTO);
        verify(cuentaBancariaRepository, times(1)).save(any(CuentaBancaria.class));
    }

    // Tests para actualizarCuentaBancaria
    @Test
    void testActualizarCuentaBancaria_conIdNulo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> {
            cuentaBancariaService.actualizarCuentaBancaria(null, TipoTransaccion.INGRESO, 100f);
        });
    }

    @Test
    void testActualizarCuentaBancaria_conMontoNulo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> {
            cuentaBancariaService.actualizarCuentaBancaria(1L, TipoTransaccion.INGRESO, null);
        });
    }

    @Test
    void testActualizarCuentaBancaria_cuandoCuentaNoExiste_lanzaExcepcion() {
        when(cuentaBancariaRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            cuentaBancariaService.actualizarCuentaBancaria(1L, TipoTransaccion.INGRESO, 100f);
        });
    }

    @Test
    void testActualizarCuentaBancaria_conGastoYSaldoInsuficiente_lanzaExcepcion() {
        when(cuentaBancariaRepository.findById(1L)).thenReturn(Optional.of(cuentaBancaria));
        assertThrows(IllegalArgumentException.class, () -> {
            cuentaBancariaService.actualizarCuentaBancaria(1L, TipoTransaccion.GASTO, 2000f);
        });
    }

    @Test
    void testActualizarCuentaBancaria_conIngreso_actualizaSaldoCorrectamente() {
        when(cuentaBancariaRepository.findById(1L)).thenReturn(Optional.of(cuentaBancaria));
        CuentaBancaria cuentaActualizada = cuentaBancariaService.actualizarCuentaBancaria(1L, TipoTransaccion.INGRESO, 500f);
        assertEquals(1500f, cuentaActualizada.getSaldoActual());
        verify(cuentaBancariaRepository, times(1)).save(cuentaBancaria);
    }

    @Test
    void testActualizarCuentaBancaria_conGastoValido_actualizaSaldoCorrectamente() {
        when(cuentaBancariaRepository.findById(1L)).thenReturn(Optional.of(cuentaBancaria));
        CuentaBancaria cuentaActualizada = cuentaBancariaService.actualizarCuentaBancaria(1L, TipoTransaccion.GASTO, 500f);
        assertEquals(500f, cuentaActualizada.getSaldoActual());
        verify(cuentaBancariaRepository, times(1)).save(cuentaBancaria);
    }

    // Tests para listarCuentasBancarias
    @Test
    void testListarCuentasBancarias_conIdEspacioTrabajoNulo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> {
            cuentaBancariaService.listarCuentasBancarias(null);
        });
    }

    @Test
    void testListarCuentasBancarias_cuandoNoExistenCuentas_retornaListaVacia() {
        when(cuentaBancariaRepository.findByEspacioTrabajo_Id(1L)).thenReturn(Collections.emptyList());
        List<CuentaBancariaListadoDTO> resultado = cuentaBancariaService.listarCuentasBancarias(1L);
        assertNotNull(resultado);
        assertEquals(0, resultado.size());
    }

    @Test
    void testListarCuentasBancarias_cuandoExistenCuentas_retornaListaDTOs() {
        when(cuentaBancariaRepository.findByEspacioTrabajo_Id(1L)).thenReturn(List.of(cuentaBancaria));
        List<CuentaBancariaListadoDTO> resultado = cuentaBancariaService.listarCuentasBancarias(1L);
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Cuenta de Ahorros", resultado.get(0).nombre());
    }

    // Tests para transaccionEntreCuentas
    @Test
    void testTransaccionEntreCuentas_conIdOrigenNulo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> {
            cuentaBancariaService.transaccionEntreCuentas(null, 2L, 100f);
        });
    }

    @Test
    void testTransaccionEntreCuentas_conIdDestinoNulo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> {
            cuentaBancariaService.transaccionEntreCuentas(1L, null, 100f);
        });
    }

    @Test
    void testTransaccionEntreCuentas_conMontoNulo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> {
            cuentaBancariaService.transaccionEntreCuentas(1L, 2L, null);
        });
    }

    @Test
    void testTransaccionEntreCuentas_cuandoCuentaOrigenNoExiste_lanzaExcepcion() {
        when(cuentaBancariaRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            cuentaBancariaService.transaccionEntreCuentas(1L, 2L, 100f);
        });
    }

    @Test
    void testTransaccionEntreCuentas_cuandoCuentaDestinoNoExiste_lanzaExcepcion() {
        when(cuentaBancariaRepository.findById(1L)).thenReturn(Optional.of(cuentaBancaria));
        when(cuentaBancariaRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            cuentaBancariaService.transaccionEntreCuentas(1L, 2L, 100f);
        });
    }

    @Test
    void testTransaccionEntreCuentas_conSaldoInsuficiente_lanzaExcepcion() {
        CuentaBancaria cuentaDestino = new CuentaBancaria();
        cuentaDestino.setId(2L);
        cuentaDestino.setSaldoActual(500f);

        when(cuentaBancariaRepository.findById(1L)).thenReturn(Optional.of(cuentaBancaria));
        when(cuentaBancariaRepository.findById(2L)).thenReturn(Optional.of(cuentaDestino));

        assertThrows(IllegalArgumentException.class, () -> {
            cuentaBancariaService.transaccionEntreCuentas(1L, 2L, 2000f);
        });
    }

    @Test
    void testTransaccionEntreCuentas_conDatosValidos_actualizaSaldosCorrectamente() {
        CuentaBancaria cuentaDestino = new CuentaBancaria();
        cuentaDestino.setId(2L);
        cuentaDestino.setSaldoActual(500f);

        when(cuentaBancariaRepository.findById(1L)).thenReturn(Optional.of(cuentaBancaria));
        when(cuentaBancariaRepository.findById(2L)).thenReturn(Optional.of(cuentaDestino));

        cuentaBancariaService.transaccionEntreCuentas(1L, 2L, 500f);

        assertEquals(500f, cuentaBancaria.getSaldoActual());
        assertEquals(1000f, cuentaDestino.getSaldoActual());
        verify(cuentaBancariaRepository, times(1)).save(cuentaBancaria);
        verify(cuentaBancariaRepository, times(1)).save(cuentaDestino);
    }
}