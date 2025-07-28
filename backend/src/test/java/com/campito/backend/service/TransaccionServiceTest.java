package com.campito.backend.service;

import com.campito.backend.dao.*;
import com.campito.backend.dto.*;
import com.campito.backend.model.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransaccionServiceTest {

    @Mock
    private TransaccionRepository transaccionRepository;
    @Mock
    private EspacioTrabajoRepository espacioRepository;
    @Mock
    private MotivoTransaccionRepository motivoRepository;
    @Mock
    private ContactoTransferenciaRepository contactoRepository;
    @Mock
    private DashboardRepository dashboardRepository;

    @InjectMocks
    private TransaccionServiceImpl transaccionService;

    private EspacioTrabajo espacioTrabajo;
    private MotivoTransaccion motivoTransaccion;
    private ContactoTransferencia contactoTransferencia;
    private Usuario usuarioAdmin;

    @BeforeEach
    void setUp() {
        usuarioAdmin = new Usuario("Admin", "admin@test.com", "foto.jpg", ProveedorAutenticacion.MANUAL, "123", "ADMIN", true, LocalDateTime.now(), LocalDateTime.now());
        usuarioAdmin.setId(1L);

        espacioTrabajo = new EspacioTrabajo("Espacio de Prueba", 1000.0f, usuarioAdmin);
        espacioTrabajo.setId(1L);

        motivoTransaccion = new MotivoTransaccion("Venta");
        motivoTransaccion.setId(1L);
        motivoTransaccion.setEspacioTrabajo(espacioTrabajo);

        contactoTransferencia = new ContactoTransferencia("Cliente A");
        contactoTransferencia.setId(1L);
        contactoTransferencia.setEspacioTrabajo(espacioTrabajo);
    }

    // Tests para registrarTransaccion

    @Test
    void registrarTransaccion_cuandoTransaccionDTONulo_entoncesLanzaExcepcion() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transaccionService.registrarTransaccion(null);
        });
        assertEquals("La transaccion no puede ser nula", exception.getMessage());
        verify(transaccionRepository, never()).save(any(Transaccion.class));
    }

    @Test
    void registrarTransaccion_cuandoIdEspacioTrabajoNulo_entoncesLanzaExcepcion() {
        TransaccionDTO dto = new TransaccionDTO(null, LocalDate.now(), 100f, TipoTransaccion.INGRESO, "Desc", "Auditor", null, 1L, null);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transaccionService.registrarTransaccion(dto);
        });
        assertEquals("El espacio de trabajo de la transaccion no puede ser nulo", exception.getMessage());
        verify(transaccionRepository, never()).save(any(Transaccion.class));
    }

    @Test
    void registrarTransaccion_cuandoIdMotivoNulo_entoncesLanzaExcepcion() {
        TransaccionDTO dto = new TransaccionDTO(null, LocalDate.now(), 100f, TipoTransaccion.INGRESO, "Desc", "Auditor", 1L, null, null);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transaccionService.registrarTransaccion(dto);
        });
        assertEquals("El motivo de la transaccion no puede ser nulo", exception.getMessage());
        verify(transaccionRepository, never()).save(any(Transaccion.class));
    }

    @Test
    void registrarTransaccion_cuandoEspacioTrabajoNoExiste_entoncesLanzaExcepcion() {
        TransaccionDTO dto = new TransaccionDTO(null, LocalDate.now(), 100f, TipoTransaccion.INGRESO, "Desc", "Auditor", 99L, 1L, null);
        when(espacioRepository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            transaccionService.registrarTransaccion(dto);
        });
        assertEquals("Espacio de trabajo con ID 99 no encontrado", exception.getMessage());
        verify(transaccionRepository, never()).save(any(Transaccion.class));
    }

    @Test
    void registrarTransaccion_cuandoMotivoNoExiste_entoncesLanzaExcepcion() {
        TransaccionDTO dto = new TransaccionDTO(null, LocalDate.now(), 100f, TipoTransaccion.INGRESO, "Desc", "Auditor", 1L, 99L, null);
        when(espacioRepository.findById(1L)).thenReturn(Optional.of(espacioTrabajo));
        when(motivoRepository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            transaccionService.registrarTransaccion(dto);
        });
        assertEquals("Motivo de transacción con ID 99 no encontrado", exception.getMessage());
        verify(transaccionRepository, never()).save(any(Transaccion.class));
    }

    @Test
    void registrarTransaccion_cuandoIdContactoExistePeroContactoNoExiste_entoncesLanzaExcepcion() {
        TransaccionDTO dto = new TransaccionDTO(null, LocalDate.now(), 100f, TipoTransaccion.INGRESO, "Desc", "Auditor", 1L, 1L, 99L);
        when(espacioRepository.findById(1L)).thenReturn(Optional.of(espacioTrabajo));
        when(motivoRepository.findById(1L)).thenReturn(Optional.of(motivoTransaccion));
        when(contactoRepository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            transaccionService.registrarTransaccion(dto);
        });
        assertEquals("Contacto de transferencia con ID 99 no encontrado", exception.getMessage());
        verify(transaccionRepository, never()).save(any(Transaccion.class));
    }

    @Test
    void registrarTransaccion_cuandoOpcionCorrecta_entoncesRegistroExitoso() {
        TransaccionDTO dto = new TransaccionDTO(null, LocalDate.now(), 100f, TipoTransaccion.INGRESO, "Desc", "Auditor", 1L, 1L, 1L);
        when(espacioRepository.findById(1L)).thenReturn(Optional.of(espacioTrabajo));
        when(motivoRepository.findById(1L)).thenReturn(Optional.of(motivoTransaccion));
        when(contactoRepository.findById(1L)).thenReturn(Optional.of(contactoTransferencia));
        when(transaccionRepository.save(any(Transaccion.class))).thenAnswer(invocation -> {
            Transaccion trans = invocation.getArgument(0);
            trans.setId(1L);
            return trans;
        });
        when(espacioRepository.save(any(EspacioTrabajo.class))).thenReturn(espacioTrabajo);

        TransaccionDTO result = transaccionService.registrarTransaccion(dto);

        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(transaccionRepository, times(1)).save(any(Transaccion.class));
        verify(espacioRepository, times(1)).save(any(EspacioTrabajo.class));
        assertEquals(1100.0f, espacioTrabajo.getSaldo()); // 1000 inicial + 100 de ingreso
    }

    // Tests para removerTransaccion

    @Test
    void removerTransaccion_cuandoIdNulo_entoncesLanzaExcepcion() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transaccionService.removerTransaccion(null);
        });
        assertEquals("El ID de la transacción no puede ser nulo", exception.getMessage());
        verify(transaccionRepository, never()).delete(any(Transaccion.class));
    }

    @Test
    void removerTransaccion_cuandoTransaccionNoExiste_entoncesLanzaExcepcion() {
        when(transaccionRepository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            transaccionService.removerTransaccion(99L);
        });
        assertEquals("Transacción con ID 99 no encontrada", exception.getMessage());
        verify(transaccionRepository, never()).delete(any(Transaccion.class));
    }

    @Test
    void removerTransaccion_cuandoOpcionCorrecta_entoncesRemueveTransaccionYActualizaEspacio() {
        Transaccion transaccion = new Transaccion(TipoTransaccion.GASTO, 50.0f, LocalDate.now(), "Gasto Test", "Auditor", LocalDateTime.now(), espacioTrabajo, motivoTransaccion, null);
        transaccion.setId(1L);
        espacioTrabajo.setSaldo(950.0f); // Saldo después del gasto

        when(transaccionRepository.findById(1L)).thenReturn(Optional.of(transaccion));
        doNothing().when(transaccionRepository).delete(any(Transaccion.class));
        when(espacioRepository.save(any(EspacioTrabajo.class))).thenReturn(espacioTrabajo);

        transaccionService.removerTransaccion(1L);

        verify(transaccionRepository, times(1)).findById(1L);
        verify(transaccionRepository, times(1)).delete(transaccion);
        verify(espacioRepository, times(1)).save(espacioTrabajo);
        assertEquals(1000.0f, espacioTrabajo.getSaldo()); // 950 + 50 de reversión
    }

    // Tests para buscarTransaccion

    @Test
    void buscarTransaccion_cuandoDatosBusquedaNulo_entoncesLanzaExcepcion() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transaccionService.buscarTransaccion(null);
        });
        assertEquals("Los datos de búsqueda no pueden ser nulos", exception.getMessage());
        verify(transaccionRepository, never()).findAll(any(Specification.class));
    }

    @Test
    void buscarTransaccion_cuandoIdEspacioTrabajoNulo_entoncesLanzaExcepcion() {
        TransaccionBusquedaDTO dto = new TransaccionBusquedaDTO(null, null, null, null, null);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transaccionService.buscarTransaccion(dto);
        });
        assertEquals("El ID del espacio de trabajo no puede ser nulo", exception.getMessage());
        verify(transaccionRepository, never()).findAll(any(Specification.class));
    }

    @Test
    void buscarTransaccion_cuandoAnioNuloYMesNoNulo_entoncesLanzaExcepcion() {
        TransaccionBusquedaDTO dto = new TransaccionBusquedaDTO(1, null, null, null, 1L);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transaccionService.buscarTransaccion(dto);
        });
        assertEquals("Si no se especifica el año, no se puede especificar el mes", exception.getMessage());
        verify(transaccionRepository, never()).findAll(any(Specification.class));
    }

    @Test
    void buscarTransaccion_cuandoBusquedaConAnio_entoncesBusquedaExitosa() {
        TransaccionBusquedaDTO dto = new TransaccionBusquedaDTO(null, 2023, null, null, 1L);
        List<Transaccion> transacciones = Collections.singletonList(new Transaccion(TipoTransaccion.INGRESO, 100f, LocalDate.of(2023, 1, 1), "Test", "User", LocalDateTime.now(), espacioTrabajo, motivoTransaccion, null));
        when(transaccionRepository.findAll(any(Specification.class))).thenReturn(transacciones);

        List<TransaccionListadoDTO> result = transaccionService.buscarTransaccion(dto);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(transaccionRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    void buscarTransaccion_cuandoBusquedaConAnioYMes_entoncesBusquedaExitosa() {
        TransaccionBusquedaDTO dto = new TransaccionBusquedaDTO(1, 2023, null, null, 1L);
        List<Transaccion> transacciones = Collections.singletonList(new Transaccion(TipoTransaccion.INGRESO, 100f, LocalDate.of(2023, 1, 1), "Test", "User", LocalDateTime.now(), espacioTrabajo, motivoTransaccion, null));
        when(transaccionRepository.findAll(any(Specification.class))).thenReturn(transacciones);

        List<TransaccionListadoDTO> result = transaccionService.buscarTransaccion(dto);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(transaccionRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    void buscarTransaccion_cuandoBusquedaConContacto_entoncesBusquedaExitosa() {
        TransaccionBusquedaDTO dto = new TransaccionBusquedaDTO(null, null, null, "Cliente A", 1L);
        List<Transaccion> transacciones = Collections.singletonList(new Transaccion(TipoTransaccion.INGRESO, 100f, LocalDate.now(), "Test", "User", LocalDateTime.now(), espacioTrabajo, motivoTransaccion, contactoTransferencia));
        when(transaccionRepository.findAll(any(Specification.class))).thenReturn(transacciones);

        List<TransaccionListadoDTO> result = transaccionService.buscarTransaccion(dto);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(transaccionRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    void buscarTransaccion_cuandoBusquedaConMotivo_entoncesBusquedaExitosa() {
        TransaccionBusquedaDTO dto = new TransaccionBusquedaDTO(null, null, "Venta", null, 1L);
        List<Transaccion> transacciones = Collections.singletonList(new Transaccion(TipoTransaccion.INGRESO, 100f, LocalDate.now(), "Test", "User", LocalDateTime.now(), espacioTrabajo, motivoTransaccion, null));
        when(transaccionRepository.findAll(any(Specification.class))).thenReturn(transacciones);

        List<TransaccionListadoDTO> result = transaccionService.buscarTransaccion(dto);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(transaccionRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    void buscarTransaccion_cuandoBusquedaSinFiltros_entoncesBusquedaExitosa() {
        TransaccionBusquedaDTO dto = new TransaccionBusquedaDTO(null, null, null, null, 1L);
        List<Transaccion> transacciones = Collections.singletonList(new Transaccion(TipoTransaccion.INGRESO, 100f, LocalDate.now(), "Test", "User", LocalDateTime.now(), espacioTrabajo, motivoTransaccion, null));
        when(transaccionRepository.findAll(any(Specification.class))).thenReturn(transacciones);

        List<TransaccionListadoDTO> result = transaccionService.buscarTransaccion(dto);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(transaccionRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    void buscarTransaccion_cuandoBusquedaSinResultados_entoncesRetornaListaVacia() {
        TransaccionBusquedaDTO dto = new TransaccionBusquedaDTO(null, null, null, null, 1L);
        when(transaccionRepository.findAll(any(Specification.class))).thenReturn(Collections.emptyList());

        List<TransaccionListadoDTO> result = transaccionService.buscarTransaccion(dto);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(transaccionRepository, times(1)).findAll(any(Specification.class));
    }

    // Tests para registrarContactoTransferencia

    @Test
    void registrarContactoTransferencia_cuandoContactoDTONulo_entoncesLanzaExcepcion() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transaccionService.registrarContactoTransferencia(null);
        });
        assertEquals("El contacto no puede ser nulo", exception.getMessage());
        verify(contactoRepository, never()).save(any(ContactoTransferencia.class));
    }

    @Test
    void registrarContactoTransferencia_cuandoNombreContactoNuloOVacio_entoncesLanzaExcepcion() {
        ContactoDTO dtoVacio = new ContactoDTO(null, "", 1L);
        IllegalArgumentException exceptionVacio = assertThrows(IllegalArgumentException.class, () -> {
            transaccionService.registrarContactoTransferencia(dtoVacio);
        });
        assertEquals("El contacto no puede ser nulo", exceptionVacio.getMessage());
        verify(contactoRepository, never()).save(any(ContactoTransferencia.class));

        ContactoDTO dtoNulo = new ContactoDTO(null, null, 1L);
        IllegalArgumentException exceptionNulo = assertThrows(IllegalArgumentException.class, () -> {
            transaccionService.registrarContactoTransferencia(dtoNulo);
        });
        assertEquals("El contacto no puede ser nulo", exceptionNulo.getMessage());
        verify(contactoRepository, never()).save(any(ContactoTransferencia.class));
    }

    @Test
    void registrarContactoTransferencia_cuandoIdEspacioTrabajoNulo_entoncesLanzaExcepcion() {
        ContactoDTO dto = new ContactoDTO(null, "Nombre Contacto", null);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transaccionService.registrarContactoTransferencia(dto);
        });
        assertEquals("El espacio de trabajo del contacto no puede ser nulo", exception.getMessage());
        verify(contactoRepository, never()).save(any(ContactoTransferencia.class));
    }

    @Test
    void registrarContactoTransferencia_cuandoEspacioTrabajoNoExiste_entoncesLanzaExcepcion() {
        ContactoDTO dto = new ContactoDTO(null, "Nombre Contacto", 99L);
        when(espacioRepository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            transaccionService.registrarContactoTransferencia(dto);
        });
        assertEquals("Espacio de trabajo con ID 99 no encontrado", exception.getMessage());
        verify(contactoRepository, never()).save(any(ContactoTransferencia.class));
    }

    @Test
    void registrarContactoTransferencia_cuandoOpcionCorrecta_entoncesRegistroExitoso() {
        ContactoDTO dto = new ContactoDTO(null, "Nuevo Contacto", 1L);
        when(espacioRepository.findById(1L)).thenReturn(Optional.of(espacioTrabajo));
        when(contactoRepository.save(any(ContactoTransferencia.class))).thenAnswer(invocation -> {
            ContactoTransferencia contacto = invocation.getArgument(0);
            contacto.setId(1L);
            return contacto;
        });

        ContactoDTO result = transaccionService.registrarContactoTransferencia(dto);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Nuevo Contacto", result.nombre());
        verify(contactoRepository, times(1)).save(any(ContactoTransferencia.class));
    }

    // Tests para nuevoMotivoTransaccion

    @Test
    void nuevoMotivoTransaccion_cuandoMotivoDTONulo_entoncesLanzaExcepcion() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transaccionService.nuevoMotivoTransaccion(null);
        });
        assertEquals("El motivo no puede ser nulo", exception.getMessage());
        verify(motivoRepository, never()).save(any(MotivoTransaccion.class));
    }

    @Test
    void nuevoMotivoTransaccion_cuandoMotivoNuloOVacio_entoncesLanzaExcepcion() {
        MotivoDTO dtoVacio = new MotivoDTO(null, "", 1L);
        IllegalArgumentException exceptionVacio = assertThrows(IllegalArgumentException.class, () -> {
            transaccionService.nuevoMotivoTransaccion(dtoVacio);
        });
        assertEquals("El motivo no puede ser nulo", exceptionVacio.getMessage());
        verify(motivoRepository, never()).save(any(MotivoTransaccion.class));

        MotivoDTO dtoNulo = new MotivoDTO(null, null, 1L);
        IllegalArgumentException exceptionNulo = assertThrows(IllegalArgumentException.class, () -> {
            transaccionService.nuevoMotivoTransaccion(dtoNulo);
        });
        assertEquals("El motivo no puede ser nulo", exceptionNulo.getMessage());
        verify(motivoRepository, never()).save(any(MotivoTransaccion.class));
    }

    @Test
    void nuevoMotivoTransaccion_cuandoIdEspacioTrabajoNulo_entoncesLanzaExcepcion() {
        MotivoDTO dto = new MotivoDTO(null, "Nuevo Motivo", null);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transaccionService.nuevoMotivoTransaccion(dto);
        });
        assertEquals("El espacio de trabajo del motivo no puede ser nulo", exception.getMessage());
        verify(motivoRepository, never()).save(any(MotivoTransaccion.class));
    }

    @Test
    void nuevoMotivoTransaccion_cuandoEspacioTrabajoNoExiste_entoncesLanzaExcepcion() {
        MotivoDTO dto = new MotivoDTO(null, "Nuevo Motivo", 99L);
        when(espacioRepository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            transaccionService.nuevoMotivoTransaccion(dto);
        });
        assertEquals("Espacio de trabajo con ID 99 no encontrado", exception.getMessage());
        verify(motivoRepository, never()).save(any(MotivoTransaccion.class));
    }

    @Test
    void nuevoMotivoTransaccion_cuandoOpcionCorrecta_entoncesRegistroExitoso() {
        MotivoDTO dto = new MotivoDTO(null, "Nuevo Motivo", 1L);
        when(espacioRepository.findById(1L)).thenReturn(Optional.of(espacioTrabajo));
        when(motivoRepository.save(any(MotivoTransaccion.class))).thenAnswer(invocation -> {
            MotivoTransaccion motivo = invocation.getArgument(0);
            motivo.setId(1L);
            return motivo;
        });

        MotivoDTO result = transaccionService.nuevoMotivoTransaccion(dto);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Nuevo Motivo", result.motivo());
        verify(motivoRepository, times(1)).save(any(MotivoTransaccion.class));
    }

    // Tests para listarContactos

    @Test
    void listarContactos_cuandoIdEspacioTrabajoNulo_entoncesLanzaExcepcion() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transaccionService.listarContactos(null);
        });
        assertEquals("El id del espacio de trabajo no puede ser nulo", exception.getMessage());
        verify(contactoRepository, never()).findByEspacioTrabajo_Id(anyLong());
    }

    @Test
    void listarContactos_cuandoNoExistenContactos_entoncesRetornaListaVacia() {
        when(contactoRepository.findByEspacioTrabajo_Id(1L)).thenReturn(Collections.emptyList());

        List<ContactoListadoDTO> result = transaccionService.listarContactos(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(contactoRepository, times(1)).findByEspacioTrabajo_Id(1L);
    }

    @Test
    void listarContactos_cuandoExistenContactos_entoncesRetornaListaConContactos() {
        List<ContactoTransferencia> contactos = new ArrayList<>();
        contactos.add(contactoTransferencia);
        when(contactoRepository.findByEspacioTrabajo_Id(1L)).thenReturn(contactos);

        List<ContactoListadoDTO> result = transaccionService.listarContactos(1L);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(contactoTransferencia.getId(), result.get(0).id());
        assertEquals(contactoTransferencia.getNombre(), result.get(0).nombre());
        verify(contactoRepository, times(1)).findByEspacioTrabajo_Id(1L);
    }

    // Tests para listarMotivos

    @Test
    void listarMotivos_cuandoIdEspacioTrabajoNulo_entoncesLanzaExcepcion() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transaccionService.listarMotivos(null);
        });
        assertEquals("El id del espacio de trabajo no puede ser nulo", exception.getMessage());
        verify(motivoRepository, never()).findByEspacioTrabajo_Id(anyLong());
    }

    @Test
    void listarMotivos_cuandoNoExistenMotivos_entoncesRetornaListaVacia() {
        when(motivoRepository.findByEspacioTrabajo_Id(1L)).thenReturn(Collections.emptyList());

        List<MotivoListadoDTO> result = transaccionService.listarMotivos(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(motivoRepository, times(1)).findByEspacioTrabajo_Id(1L);
    }

    @Test
    void listarMotivos_cuandoExistenMotivos_entoncesRetornaListaConMotivos() {
        List<MotivoTransaccion> motivos = new ArrayList<>();
        motivos.add(motivoTransaccion);
        when(motivoRepository.findByEspacioTrabajo_Id(1L)).thenReturn(motivos);

        List<MotivoListadoDTO> result = transaccionService.listarMotivos(1L);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(motivoTransaccion.getId(), result.get(0).id());
        assertEquals(motivoTransaccion.getMotivo(), result.get(0).motivo());
        verify(motivoRepository, times(1)).findByEspacioTrabajo_Id(1L);
    }

    // Tests para buscarTransaccionesRecientes

    @Test
    void buscarTransaccionesRecientes_cuandoIdEspacioTrabajoNulo_entoncesLanzaExcepcion() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transaccionService.buscarTransaccionesRecientes(null);
        });
        assertEquals("El id del espacio de trabajo no puede ser nulo", exception.getMessage());
        verify(transaccionRepository, never()).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void buscarTransaccionesRecientes_cuandoNoExistenTransacciones_entoncesRetornaListaVacia() {
        when(transaccionRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.emptyList()));

        List<TransaccionListadoDTO> result = transaccionService.buscarTransaccionesRecientes(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(transaccionRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void buscarTransaccionesRecientes_cuandoOpcionCorrecta_entoncesRetornaUltimas6Transacciones() {
        List<Transaccion> transacciones = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            transacciones.add(new Transaccion(TipoTransaccion.INGRESO, 100f, LocalDate.now(), "Desc " + i, "User", LocalDateTime.now().minusMinutes(i), espacioTrabajo, motivoTransaccion, null));
        }
        // Asegurarse de que las transacciones estén ordenadas por fechaCreacion descendente para el mock
        transacciones.sort((t1, t2) -> t2.getFechaCreacion().compareTo(t1.getFechaCreacion()));

        when(transaccionRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(new PageImpl<>(transacciones.subList(0, 6)));

        List<TransaccionListadoDTO> result = transaccionService.buscarTransaccionesRecientes(1L);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(6, result.size());
        assertEquals("Desc 0", result.get(0).descripcion()); // La más reciente
        verify(transaccionRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }
}