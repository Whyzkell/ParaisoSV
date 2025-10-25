package sv.edu.udb.demo.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import sv.edu.udb.demo.model.Proyectos; // Asegúrate que la ruta sea correcta

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class ProyectosRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProyectosRepository proyectosRepository;

    @Test
    public void cuandoGuardaProyecto_entoncesRetornaProyectoGuardado() {
        // 1. Arrange (Preparar)
        Proyectos proyecto = Proyectos.builder()
                .tit("Proyecto de Reforestación")
                .descr("Plantando árboles en la comunidad.")
                .img("http://example.com/arbol.png")
                .build();

        // 2. Act (Actuar)
        Proyectos proyectoGuardado = proyectosRepository.save(proyecto);

        // 3. Assert (Verificar)
        assertThat(proyectoGuardado).isNotNull();
        assertThat(proyectoGuardado.getId()).isGreaterThan(0);
        assertThat(proyectoGuardado.getTit()).isEqualTo("Proyecto de Reforestación");
    }

    @Test
    public void cuandoFindById_entoncesRetornaProyecto() {
        // 1. Arrange (Preparar)
        Proyectos proyecto = Proyectos.builder()
                .tit("Limpieza de Playa")
                .descr("Recolección de desechos en la costa.")
                .build();
        // Usamos persistAndFlush para que @CreationTimestamp se active
        Proyectos proyectoEnDB = entityManager.persistAndFlush(proyecto);

        // 2. Act (Actuar)
        Optional<Proyectos> proyectoEncontrado = proyectosRepository.findById(proyectoEnDB.getId());

        // 3. Assert (Verificar)
        assertThat(proyectoEncontrado).isPresent();
        assertThat(proyectoEncontrado.get().getId()).isEqualTo(proyectoEnDB.getId());
        assertThat(proyectoEncontrado.get().getTit()).isEqualTo("Limpieza de Playa");
        
        // Verificamos que @CreationTimestamp funcionó
        assertThat(proyectoEncontrado.get().getCreadoEn()).isNotNull();
    }

    @Test
    public void cuandoFindAll_entoncesRetornaListaDeProyectos() {
        // 1. Arrange (Preparar)
        Proyectos proyecto1 = Proyectos.builder().tit("Proyecto A").descr("...").build();
        Proyectos proyecto2 = Proyectos.builder().tit("Proyecto B").descr("...").build();

        entityManager.persist(proyecto1);
        entityManager.persist(proyecto2);
        entityManager.flush();

        // 2. Act (Actuar)
        List<Proyectos> proyectos = proyectosRepository.findAll();

        // 3. Assert (Verificar)
        assertThat(proyectos).isNotNull();
        assertThat(proyectos.size()).isEqualTo(2);
        assertThat(proyectos).extracting(Proyectos::getTit).contains("Proyecto A", "Proyecto B");
    }

    @Test
    public void cuandoActualizaProyecto_entoncesRetornaDatosActualizados() {
        // 1. Arrange (Preparar)
        Proyectos proyecto = Proyectos.builder().tit("Título Original").descr("...").build();
        Proyectos proyectoEnDB = entityManager.persistAndFlush(proyecto);
        
        // Guardamos el timestamp original
        LocalDateTime fechaCreacionOriginal = proyectoEnDB.getCreadoEn();

        // 2. Act (Actuar)
        Proyectos proyectoParaActualizar = proyectosRepository.findById(proyectoEnDB.getId()).get();
        proyectoParaActualizar.setTit("Título Actualizado");
        proyectoParaActualizar.setDescr("Descripción actualizada");
        proyectosRepository.save(proyectoParaActualizar);
        
        entityManager.flush();

        // 3. Assert (Verificar)
        Proyectos proyectoActualizado = proyectosRepository.findById(proyectoEnDB.getId()).get();
        assertThat(proyectoActualizado.getTit()).isEqualTo("Título Actualizado");
        assertThat(proyectoActualizado.getDescr()).isEqualTo("Descripción actualizada");
        
        // Verificamos que 'creadoEn' NO se actualizó (gracias a updatable = false)
        assertThat(proyectoActualizado.getCreadoEn()).isNotNull();
        assertThat(proyectoActualizado.getCreadoEn()).isEqualTo(fechaCreacionOriginal);
    }

    @Test
    public void cuandoDeleteById_entoncesProyectoYaNoExiste() {
        // 1. Arrange (Preparar)
        Proyectos proyecto = Proyectos.builder().tit("Proyecto a Borrar").descr("...").build();
        Proyectos proyectoEnDB = entityManager.persistAndFlush(proyecto);
        Integer id = proyectoEnDB.getId();

        // 2. Act (Actuar)
        proyectosRepository.deleteById(id);

        // 3. Assert (Verificar)
        Optional<Proyectos> proyectoBorrado = proyectosRepository.findById(id);
        assertThat(proyectoBorrado).isNotPresent();
    }
}