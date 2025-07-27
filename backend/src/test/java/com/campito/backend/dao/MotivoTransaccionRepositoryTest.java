package com.campito.backend.dao;

import com.campito.backend.model.EspacioTrabajo;
import com.campito.backend.model.MotivoTransaccion;
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
public class MotivoTransaccionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MotivoTransaccionRepository motivoTransaccionRepository;

    private EspacioTrabajo espacioTrabajoConMotivos;
    private EspacioTrabajo espacioTrabajoSinMotivos;

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

        espacioTrabajoConMotivos = new EspacioTrabajo("Espacio 1", 1000.0f, usuarioAdmin);
        entityManager.persist(espacioTrabajoConMotivos);

        espacioTrabajoSinMotivos = new EspacioTrabajo("Espacio 2", 500.0f, usuarioAdmin);
        entityManager.persist(espacioTrabajoSinMotivos);

        MotivoTransaccion motivo = new MotivoTransaccion("Venta de Soja");
        motivo.setEspacioTrabajo(espacioTrabajoConMotivos);
        entityManager.persist(motivo);

        entityManager.flush();
    }

    @Test
    public void cuandoBuscarPorEspacioTrabajo_entoncesRetornarMotivos() {
        // when
        List<MotivoTransaccion> encontrados = motivoTransaccionRepository.findByEspacioTrabajo_Id(espacioTrabajoConMotivos.getId());

        // then
        assertThat(encontrados).isNotEmpty();
        assertThat(encontrados.size()).isEqualTo(1);
        assertThat(encontrados.get(0).getMotivo()).isEqualTo("Venta de Soja");
    }

    @Test
    public void cuandoBuscarPorEspacioTrabajoSinMotivos_entoncesRetornarListaVacia() {
        // when
        List<MotivoTransaccion> encontrados = motivoTransaccionRepository.findByEspacioTrabajo_Id(espacioTrabajoSinMotivos.getId());

        // then
        assertThat(encontrados).isEmpty();
    }
}