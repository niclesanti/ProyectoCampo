package com.campito.backend.dao;

import com.campito.backend.model.ContactoTransferencia;
import com.campito.backend.model.EspacioTrabajo;
import com.campito.backend.model.ProveedorAutenticacion;
import com.campito.backend.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ContactoTransferenciaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ContactoTransferenciaRepository contactoTransferenciaRepository;

    private EspacioTrabajo espacioTrabajoConContactos;
    private EspacioTrabajo espacioTrabajoSinContactos;

    @BeforeEach
    void setup() {
        Usuario usuarioAdmin = new Usuario();
        usuarioAdmin.setEmail("admin@test.com");
        usuarioAdmin.setNombre("Admin User");
        usuarioAdmin.setProveedor(ProveedorAutenticacion.MANUAL);
        usuarioAdmin.setRol("ADMIN");
        usuarioAdmin.setActivo(true);
        usuarioAdmin.setFechaRegistro(LocalDateTime.now());
        entityManager.persist(usuarioAdmin);

        espacioTrabajoConContactos = new EspacioTrabajo("Espacio 1", 1000.0f, usuarioAdmin);
        entityManager.persist(espacioTrabajoConContactos);

        espacioTrabajoSinContactos = new EspacioTrabajo("Espacio 2", 500.0f, usuarioAdmin);
        entityManager.persist(espacioTrabajoSinContactos);

        ContactoTransferencia contacto = new ContactoTransferencia("Contacto 1");
        contacto.setEspacioTrabajo(espacioTrabajoConContactos);
        entityManager.persist(contacto);

        entityManager.flush();
    }

    @Test
    public void cuandoBuscarPorEspacioTrabajo_entoncesRetornarContactos() {
        // when
        List<ContactoTransferencia> encontrados = contactoTransferenciaRepository.findByEspacioTrabajo_Id(espacioTrabajoConContactos.getId());

        // then
        assertThat(encontrados).isNotEmpty();
        assertThat(encontrados.size()).isEqualTo(1);
    }

    @Test
    public void cuandoBuscarPorEspacioTrabajoSinContactos_entoncesRetornarListaVacia() {
        // when
        List<ContactoTransferencia> encontrados = contactoTransferenciaRepository.findByEspacioTrabajo_Id(espacioTrabajoSinContactos.getId());

        // then
        assertThat(encontrados).isEmpty();
    }
}