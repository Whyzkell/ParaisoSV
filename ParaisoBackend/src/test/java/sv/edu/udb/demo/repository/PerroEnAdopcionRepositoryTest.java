package sv.edu.udb.demo.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import sv.edu.udb.demo.model.PerroEnAdopcion; // Asegúrate que la ruta sea correcta

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class PerroEnAdopcionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PerroEnAdopcionRepository perroEnAdopcionRepository;

    @Test
    public void cuandoGuardaPerro_entoncesRetornaPerroGuardado() {
        // 1. Arrange (Preparar)
        PerroEnAdopcion perro = PerroEnAdopcion.builder()
                .nombre("Firulais")
                .raza("Mestizo")
                .edad(2)
                .descr("Juguetón y amigable")
                .build();

        // 2. Act (Actuar)
        PerroEnAdopcion perroGuardado = perroEnAdopcionRepository.save(perro);

        // 3. Assert (Verificar)
        assertThat(perroGuardado).isNotNull();
        assertThat(perroGuardado.getId()).isGreaterThan(0);
        assertThat(perroGuardado.getNombre()).isEqualTo("Firulais");
        assertThat(perroGuardado.getRaza()).isEqualTo("Mestizo");
    }

    @Test
    public void cuandoFindById_entoncesRetornaPerro() {
        // 1. Arrange (Preparar)
        PerroEnAdopcion perro = PerroEnAdopcion.builder()
                .nombre("Max")
                .raza("Pastor Alemán")
                .edad(5)
                .img("http://example.com/max.png")
                .build();
        // Usamos entityManager.persistAndFlush() para asegurar que @CreationTimestamp se active
        PerroEnAdopcion perroEnDB = entityManager.persistAndFlush(perro);

        // 2. Act (Actuar)
        Optional<PerroEnAdopcion> perroEncontrado = perroEnAdopcionRepository.findById(perroEnDB.getId());

        // 3. Assert (Verificar)
        assertThat(perroEncontrado).isPresent();
        assertThat(perroEncontrado.get().getId()).isEqualTo(perroEnDB.getId());
        assertThat(perroEncontrado.get().getNombre()).isEqualTo("Max");
        
        // Verificamos que @CreationTimestamp funcionó
        assertThat(perroEncontrado.get().getCreadoEn()).isNotNull();
        // Comparamos que sea una fecha/hora reciente
        assertThat(perroEncontrado.get().getCreadoEn()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    public void cuandoFindAll_entoncesRetornaListaDePerros() {
        // 1. Arrange (Preparar)
        PerroEnAdopcion perro1 = PerroEnAdopcion.builder().nombre("Luna").raza("Husky").edad(3).build();
        PerroEnAdopcion perro2 = PerroEnAdopcion.builder().nombre("Rocky").raza("Boxer").edad(1).build();
        
        entityManager.persist(perro1);
        entityManager.persist(perro2);
        entityManager.flush(); // Sincroniza con la BD de prueba

        // 2. Act (Actuar)
        List<PerroEnAdopcion> perros = perroEnAdopcionRepository.findAll();

        // 3. Assert (Verificar)
        assertThat(perros).isNotNull();
        assertThat(perros.size()).isEqualTo(2);
        // 'extracting' es útil para verificar propiedades de una lista
        assertThat(perros).extracting(PerroEnAdopcion::getNombre).contains("Luna", "Rocky");
    }

    @Test
    public void cuandoActualizaPerro_entoncesRetornaDatosActualizados() {
        // 1. Arrange (Preparar)
        PerroEnAdopcion perro = PerroEnAdopcion.builder().nombre("Toby").raza("Beagle").edad(4).build();
        PerroEnAdopcion perroEnDB = entityManager.persistAndFlush(perro);
        
        // Guardamos el timestamp original
        LocalDateTime fechaCreacionOriginal = perroEnDB.getCreadoEn(); 

        // 2. Act (Actuar)
        // Buscamos la entidad, la modificamos y la guardamos
        PerroEnAdopcion perroParaActualizar = perroEnAdopcionRepository.findById(perroEnDB.getId()).get();
        perroParaActualizar.setNombre("Toby Actualizado");
        perroParaActualizar.setEdad(5);
        PerroEnAdopcion perroActualizado = perroEnAdopcionRepository.save(perroParaActualizar);
        
        // Forzamos la sincronización para estar seguros
        entityManager.flush(); 

        // 3. Assert (Verificar)
        assertThat(perroActualizado.getNombre()).isEqualTo("Toby Actualizado");
        assertThat(perroActualizado.getEdad()).isEqualTo(5);
        
        // Verificamos que 'creadoEn' NO se actualizó (gracias a updatable = false)
        assertThat(perroActualizado.getCreadoEn()).isNotNull();
        assertThat(perroActualizado.getCreadoEn()).isEqualTo(fechaCreacionOriginal);
    }

    @Test
    public void cuandoDeleteById_entoncesPerroYaNoExiste() {
        // 1. Arrange (Preparar)
        PerroEnAdopcion perro = PerroEnAdopcion.builder().nombre("Borrame").raza("Chihuahua").edad(1).build();
        PerroEnAdopcion perroEnDB = entityManager.persistAndFlush(perro);
        Integer id = perroEnDB.getId(); // Guardamos el ID

        // 2. Act (Actuar)
        perroEnAdopcionRepository.deleteById(id);
        
        // 3. Assert (Verificar)
        Optional<PerroEnAdopcion> perroBorrado = perroEnAdopcionRepository.findById(id);
        assertThat(perroBorrado).isNotPresent(); // Verificamos que ya no está
    }
}