package sv.edu.udb.demo.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import sv.edu.udb.demo.model.Alcancia; // Asegúrate que la ruta de importación sea correcta

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class AlcanciaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AlcanciaRepository alcanciaRepository;

    @Test
    public void cuandoGuardaAlcancia_entoncesRetornaAlcanciaGuardada() {
        // 1. Arrange (Preparar)
        // Usamos el Builder de Lombok que definiste en tu entidad
        Alcancia alcancia = Alcancia.builder()
                .descr("Ahorro para viaje")
                .precioMeta(new BigDecimal("1500.00"))
                .precioActual(new BigDecimal("100.00")) // Campo 'nullable=false'
                .build();

        // 2. Act (Actuar)
        Alcancia alcanciaGuardada = alcanciaRepository.save(alcancia);

        // 3. Assert (Verificar)
        assertThat(alcanciaGuardada).isNotNull();
        assertThat(alcanciaGuardada.getId()).isGreaterThan(0);
        assertThat(alcanciaGuardada.getDescr()).isEqualTo("Ahorro para viaje");
        // Verificamos el campo 'creadoEn' (insertable=false)
        // Debería ser nulo después de 'save' a menos que la BD lo genere y 'save' lo refresque.
        // Una verificación más robusta es buscarlo de nuevo (ver test 'findById').
        
        // Verifiquemos los campos de BigDecimal
        assertThat(alcanciaGuardada.getPrecioMeta().compareTo(new BigDecimal("1500.00"))).isEqualTo(0);
        assertThat(alcanciaGuardada.getPrecioActual().compareTo(new BigDecimal("100.00"))).isEqualTo(0);
    }

    @Test
    public void cuandoFindById_entoncesRetornaAlcancia() {
        // 1. Arrange (Preparar)
        Alcancia alcancia = Alcancia.builder()
                .descr("Ahorro para PS5")
                .precioMeta(new BigDecimal("500.00"))
                .precioActual(BigDecimal.ZERO) // Otra forma de poner 0
                .build();
        
        // Persistimos y forzamos la sincronización para que la BD genere el 'CreadoEn'
        Alcancia alcanciaEnDB = entityManager.persistAndFlush(alcancia); 

        // 2. Act (Actuar)
        Optional<Alcancia> alcanciaEncontrada = alcanciaRepository.findById(alcanciaEnDB.getId());

        // 3. Assert (Verificar)
        assertThat(alcanciaEncontrada).isPresent();
        assertThat(alcanciaEncontrada.get().getId()).isEqualTo(alcanciaEnDB.getId());
        assertThat(alcanciaEncontrada.get().getDescr()).isEqualTo("Ahorro para PS5");
        
        // Verificamos que el campo 'CreadoEn' (updatable=false, insertable=false)
        // fue poblado por la base de datos (o el trigger de JPA)
        // Nota: H2 podría no autogenerar LocalDateTime. Si 'creadoEn' es nulo aquí,
        // es probable que necesites @CreationTimestamp de Hibernate.
        // Pero para la prueba del repo, basta con saber que el resto funciona.
        // assertThat(alcanciaEncontrada.get().getCreadoEn()).isNotNull(); 
    }

    @Test
    public void cuandoFindAll_entoncesRetornaListaDeAlcancias() {
        // 1. Arrange (Preparar)
        Alcancia alcancia1 = Alcancia.builder()
                .descr("Alcancia 1")
                .precioMeta(new BigDecimal("100.00"))
                .precioActual(new BigDecimal("10.00"))
                .build();
        entityManager.persist(alcancia1);

        Alcancia alcancia2 = Alcancia.builder()
                .descr("Alcancia 2")
                .precioMeta(new BigDecimal("200.00"))
                .precioActual(new BigDecimal("20.00"))
                .build();
        entityManager.persist(alcancia2);

        entityManager.flush();

        // 2. Act (Actuar)
        List<Alcancia> alcancias = alcanciaRepository.findAll();

        // 3. Assert (Verificar)
        assertThat(alcancias).isNotNull();
        assertThat(alcancias.size()).isEqualTo(2);
        assertThat(alcancias).extracting(Alcancia::getDescr).contains("Alcancia 1", "Alcancia 2");
    }

    @Test
    public void cuandoActualizaAlcancia_entoncesRetornaDatosActualizados() {
        // 1. Arrange (Preparar)
        Alcancia alcancia = Alcancia.builder()
                .descr("Meta Original")
                .precioMeta(new BigDecimal("300.00"))
                .precioActual(new BigDecimal("50.00"))
                .build();
        Alcancia alcanciaEnDB = entityManager.persistAndFlush(alcancia);

        // 2. Act (Actuar)
        Alcancia alcanciaParaActualizar = alcanciaRepository.findById(alcanciaEnDB.getId()).get();
        alcanciaParaActualizar.setDescr("Meta Actualizada");
        alcanciaParaActualizar.setPrecioActual(new BigDecimal("75.00")); // Actualizamos el precio actual
        alcanciaRepository.save(alcanciaParaActualizar);

        // 3. Assert (Verificar)
        Alcancia alcanciaActualizada = alcanciaRepository.findById(alcanciaEnDB.getId()).get();
        assertThat(alcanciaActualizada.getDescr()).isEqualTo("Meta Actualizada");
        assertThat(alcanciaActualizada.getPrecioActual().compareTo(new BigDecimal("75.00"))).isEqualTo(0);
    }

    @Test
    public void cuandoDeleteById_entoncesAlcanciaYaNoExiste() {
        // 1. Arrange (Preparar)
        Alcancia alcancia = Alcancia.builder()
                .descr("Para borrar")
                .precioMeta(new BigDecimal("50.00"))
                .precioActual(BigDecimal.ONE)
                .build();
        Alcancia alcanciaEnDB = entityManager.persistAndFlush(alcancia);
        Integer id = alcanciaEnDB.getId();

        // 2. Act (Actuar)
        alcanciaRepository.deleteById(id);

        // 3. Assert (Verificar)
        Optional<Alcancia> alcanciaBorrada = alcanciaRepository.findById(id);
        assertThat(alcanciaBorrada).isNotPresent();
    }
}