package com.campito.backend.dao;

import com.campito.backend.model.ProveedorAutenticacion;
import com.campito.backend.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UsuarioRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario usuario;

    @BeforeEach
    void setup() {
        usuario = new Usuario();
        usuario.setEmail("test@test.com");
        usuario.setNombre("Test User");
        usuario.setProveedor(ProveedorAutenticacion.MANUAL);
        usuario.setRol("USUARIO");
        usuario.setActivo(true);
        usuario.setFechaRegistro(LocalDateTime.now());
        entityManager.persist(usuario);
        entityManager.flush();
    }

    @Test
    public void cuandoBuscarPorEmailYProveedor_entoncesRetornarUsuario() {
        // when
        Optional<Usuario> encontrado = usuarioRepository.findByEmailAndProveedor(usuario.getEmail(), usuario.getProveedor());

        // then
        assertThat(encontrado.isPresent()).isTrue();
        assertThat(encontrado.get().getEmail()).isEqualTo(usuario.getEmail());
    }

    @Test
    public void cuandoBuscarPorEmailYProveedorNoExistente_entoncesRetornarVacio() {
        // when
        Optional<Usuario> encontrado = usuarioRepository.findByEmailAndProveedor("noexiste@test.com", ProveedorAutenticacion.MANUAL);

        // then
        assertThat(encontrado.isEmpty()).isTrue();
    }

}