package com.campito.backend.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.campito.backend.model.CuentaBancaria;
import com.campito.backend.model.EspacioTrabajo;
import com.campito.backend.model.ProveedorAutenticacion;
import com.campito.backend.model.Usuario;

@DataJpaTest
public class CuentaBancariaRepositoryTest {

    @Autowired
    private CuentaBancariaRepository cuentaBancariaRepository;

    @Autowired
    private TestEntityManager entityManager;

    private EspacioTrabajo espacioTrabajoConCuentas;
    private EspacioTrabajo espacioTrabajoSinCuentas;

    @BeforeEach
    void setUp() {
        Usuario usuarioAdmin = new Usuario();
        usuarioAdmin.setEmail("admin@test.com");
        usuarioAdmin.setNombre("Admin User");
        usuarioAdmin.setProveedor(ProveedorAutenticacion.MANUAL);
        usuarioAdmin.setRol("ADMIN");
        usuarioAdmin.setActivo(true);
        usuarioAdmin.setFechaRegistro(LocalDateTime.now());
        entityManager.persist(usuarioAdmin);

        // Configurar espacio de trabajo CON cuentas
        espacioTrabajoConCuentas = new EspacioTrabajo("Mi Espacio de Trabajo con Cuentas", 0f, usuarioAdmin);
        entityManager.persist(espacioTrabajoConCuentas);

        CuentaBancaria cuenta1 = new CuentaBancaria();
        cuenta1.setNombre("Cuenta de Ahorros");
        cuenta1.setEntidadFinanciera("Banco Ejemplo");
        cuenta1.setEspacioTrabajo(espacioTrabajoConCuentas);
        cuenta1.setSaldoActual(0f);
        entityManager.persist(cuenta1);

        CuentaBancaria cuenta2 = new CuentaBancaria();
        cuenta2.setNombre("Cuenta Corriente");
        cuenta2.setEntidadFinanciera("Banco Ejemplo");
        cuenta2.setEspacioTrabajo(espacioTrabajoConCuentas);
        cuenta2.setSaldoActual(0f);
        entityManager.persist(cuenta2);

        // Configurar espacio de trabajo SIN cuentas
        espacioTrabajoSinCuentas = new EspacioTrabajo("Mi Espacio de Trabajo sin Cuentas", 0f, usuarioAdmin);
        entityManager.persist(espacioTrabajoSinCuentas);

        entityManager.flush();
    }

    @Test
    void testBuscarCuentasBancariasPorEspacioDeTrabajoYQueExistaAlguno() {
        // Act
        List<CuentaBancaria> cuentas = cuentaBancariaRepository.findByEspacioTrabajo_Id(espacioTrabajoConCuentas.getId());

        // Assert
        assertFalse(cuentas.isEmpty());
        assertEquals(2, cuentas.size());
    }

    @Test
    void testBuscarCuentasBancariasPorEspacioDeTrabajoYQueNoExistaNinguno() {
        // Act
        List<CuentaBancaria> cuentas = cuentaBancariaRepository.findByEspacioTrabajo_Id(espacioTrabajoSinCuentas.getId());

        // Assert
        assertTrue(cuentas.isEmpty());
    }
}