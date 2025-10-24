package sv.edu.udb.demo.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import sv.edu.udb.demo.model.Alcancia;
import sv.edu.udb.demo.model.Donaciones;
import sv.edu.udb.demo.model.Usuario; // Asumiendo que esta es la ruta de tu entidad Usuario

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DonacionesRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DonacionesRepository donacionesRepository;

    // --- Helper Method ---
    // Este metodo crea y persiste las dependencias necesarias.
    // Devuelve un array con el Usuario y la Alcancia ya guardados.
    private Object[] crearDependencias() {
        // 1. Crear Usuario (Ajustado a tu modelo Usuario.java)
        Usuario usuario = Usuario.builder()
                .nombre("Usuario de Prueba")
                .correo("test@test.com")     // <-- CORREGIDO
                .password("password")       // <-- CORREGIDO
                .build();
        Usuario usuarioGuardado = entityManager.persistAndFlush(usuario);

        // 2. Crear Alcancia (Usando el modelo que ya me diste)
        Alcancia alcancia = Alcancia.builder()
                .descr("Alcancia de Prueba")
                .precioMeta(new BigDecimal("1000.00"))
                .precioActual(BigDecimal.ZERO)
                .build();
        Alcancia alcanciaGuardada = entityManager.persistAndFlush(alcancia);

        return new Object[]{usuarioGuardado, alcanciaGuardada};
    }


    @Test
    public void cuandoGuardaDonacion_entoncesRetornaDonacionGuardada() {
        // 1. Arrange (Preparar dependencias)
        Object[] dependencias = crearDependencias();
        Usuario usuario = (Usuario) dependencias[0];
        Alcancia alcancia = (Alcancia) dependencias[1];

        // Crear la donación
        Donaciones donacion = Donaciones.builder()
                .usuario(usuario)
                .alcancia(alcancia)
                .cantidadDonada(new BigDecimal("50.00"))
                .build();

        // 2. Act (Actuar)
        Donaciones donacionGuardada = donacionesRepository.save(donacion);

        // 3. Assert (Verificar)
        assertThat(donacionGuardada).isNotNull();
        assertThat(donacionGuardada.getId()).isGreaterThan(0);
        assertThat(donacionGuardada.getCantidadDonada().compareTo(new BigDecimal("50.00"))).isEqualTo(0);
        // Verificar que las relaciones se guardaron correctamente
        assertThat(donacionGuardada.getUsuario().getId()).isEqualTo(usuario.getId());
        assertThat(donacionGuardada.getAlcancia().getId()).isEqualTo(alcancia.getId());
    }

    @Test
    public void cuandoFindById_entoncesRetornaDonacion() {
        // 1. Arrange (Preparar)
        Object[] dependencias = crearDependencias();
        Usuario usuario = (Usuario) dependencias[0];
        Alcancia alcancia = (Alcancia) dependencias[1];

        Donaciones donacion = Donaciones.builder()
                .usuario(usuario)
                .alcancia(alcancia)
                .cantidadDonada(new BigDecimal("120.50"))
                .build();
        // Persistimos la donación de prueba
        Donaciones donacionEnDB = entityManager.persistAndFlush(donacion);

        // 2. Act (Actuar)
        Optional<Donaciones> donacionEncontrada = donacionesRepository.findById(donacionEnDB.getId());

        // 3. Assert (Verificar)
        assertThat(donacionEncontrada).isPresent();
        assertThat(donacionEncontrada.get().getId()).isEqualTo(donacionEnDB.getId());
        assertThat(donacionEncontrada.get().getCantidadDonada().compareTo(new BigDecimal("120.50"))).isEqualTo(0);
    }

    @Test
    public void cuandoFindAll_entoncesRetornaListaDeDonaciones() {
        // 1. Arrange (Preparar)
        Object[] dependencias = crearDependencias();
        Usuario usuario = (Usuario) dependencias[0];
        Alcancia alcancia = (Alcancia) dependencias[1];

        Donaciones donacion1 = Donaciones.builder()
                .usuario(usuario)
                .alcancia(alcancia)
                .cantidadDonada(new BigDecimal("10.00"))
                .build();
        entityManager.persist(donacion1);

        Donaciones donacion2 = Donaciones.builder()
                .usuario(usuario) // Puede ser el mismo usuario y alcancia
                .alcancia(alcancia)
                .cantidadDonada(new BigDecimal("20.00"))
                .build();
        entityManager.persist(donacion2);

        entityManager.flush();

        // 2. Act (Actuar)
        List<Donaciones> donaciones = donacionesRepository.findAll();

        // 3. Assert (Verificar)
        assertThat(donaciones).isNotNull();
        assertThat(donaciones.size()).isEqualTo(2);
        assertThat(donaciones).extracting(Donaciones::getCantidadDonada)
                .containsExactlyInAnyOrder(new BigDecimal("10.00"), new BigDecimal("20.00"));
    }

    @Test
    public void cuandoDeleteById_entoncesDonacionYaNoExiste() {
        // 1. Arrange (Preparar)
        Object[] dependencias = crearDependencias();
        Usuario usuario = (Usuario) dependencias[0];
        Alcancia alcancia = (Alcancia) dependencias[1];

        Donaciones donacion = Donaciones.builder()
                .usuario(usuario)
                .alcancia(alcancia)
                .cantidadDonada(new BigDecimal("99.00"))
                .build();
        Donaciones donacionEnDB = entityManager.persistAndFlush(donacion);
        Integer id = donacionEnDB.getId(); // Guardamos el ID

        // 2. Act (Actuar)
        donacionesRepository.deleteById(id);

        // 3. Assert (Verificar)
        Optional<Donaciones> donacionBorrada = donacionesRepository.findById(id);
        assertThat(donacionBorrada).isNotPresent();
    }
}