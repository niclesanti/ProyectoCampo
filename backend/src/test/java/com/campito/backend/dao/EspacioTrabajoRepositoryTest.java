package com.campito.backend.dao;

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
public class EspacioTrabajoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EspacioTrabajoRepository espacioTrabajoRepository;

    private Usuario usuarioParticipante;
    private Usuario usuarioNoParticipante;

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

        usuarioParticipante = new Usuario();
        usuarioParticipante.setEmail("participante@test.com");
        usuarioParticipante.setNombre("Participante User");
        usuarioParticipante.setProveedor(ProveedorAutenticacion.MANUAL);
        usuarioParticipante.setRol("USUARIO");
        usuarioParticipante.setActivo(true);
        usuarioParticipante.setFechaRegistro(LocalDateTime.now());
        entityManager.persist(usuarioParticipante);

        usuarioNoParticipante = new Usuario();
        usuarioNoParticipante.setEmail("noparticipante@test.com");
        usuarioNoParticipante.setNombre("No Participante User");
        usuarioNoParticipante.setProveedor(ProveedorAutenticacion.MANUAL);
        usuarioNoParticipante.setRol("USUARIO");
        usuarioNoParticipante.setActivo(true);
        usuarioNoParticipante.setFechaRegistro(LocalDateTime.now());
        entityManager.persist(usuarioNoParticipante);

        EspacioTrabajo espacioTrabajo = new EspacioTrabajo("Espacio de Prueba", 1000.0f, usuarioAdmin);
        espacioTrabajo.addUsuariosParticipante(usuarioParticipante);
        entityManager.persist(espacioTrabajo);

        entityManager.flush();
    }

    @Test
    public void cuandoBuscarPorUsuarioParticipante_entoncesRetornarEspacios() {
        // when
        List<EspacioTrabajo> encontrados = espacioTrabajoRepository.findByUsuariosParticipantes_Id(usuarioParticipante.getId());

        // then
        assertThat(encontrados).isNotEmpty();
        assertThat(encontrados.size()).isEqualTo(1);
    }

    @Test
    public void cuandoBuscarPorUsuarioNoParticipante_entoncesRetornarListaVacia() {
        // when
        List<EspacioTrabajo> encontrados = espacioTrabajoRepository.findByUsuariosParticipantes_Id(usuarioNoParticipante.getId());

        // then
        assertThat(encontrados).isEmpty();
    }
}