package sv.edu.udb.demo.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
// Importante para probar la restricción 'unique'
import org.springframework.dao.DataIntegrityViolationException; 
import sv.edu.udb.demo.model.Usuario;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class UsuarioRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void cuandoGuardaUsuario_entoncesRetornaUsuarioGuardado() {
        // 1. Arrange
        Usuario usuario = Usuario.builder()
                .nombre("Juan Perez")
                .correo("juan.perez@test.com")
                .password("un-password-hasheado-123")
                .rol("USER")
                .build();

        // 2. Act
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        // 3. Assert
        assertThat(usuarioGuardado).isNotNull();
        assertThat(usuarioGuardado.getId()).isGreaterThan(0);
        assertThat(usuarioGuardado.getNombre()).isEqualTo("Juan Perez");
    }

    @Test
    public void cuandoFindById_entoncesRetornaUsuario() {
        // 1. Arrange
        Usuario usuario = Usuario.builder()
                .nombre("Ana Lopez")
                .correo("ana.lopez@test.com")
                .password("pass-456")
                .rol("ADMIN")
                .build();
        Usuario usuarioEnDB = entityManager.persistAndFlush(usuario);

        // 2. Act
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(usuarioEnDB.getId());

        // 3. Assert
        assertThat(usuarioEncontrado).isPresent();
        assertThat(usuarioEncontrado.get().getCorreo()).isEqualTo("ana.lopez@test.com");
        // Verificamos que @CreationTimestamp funcionó
        assertThat(usuarioEncontrado.get().getCreadoEn()).isNotNull();
    }

    @Test
    public void cuandoActualizaUsuario_entoncesNoSeActualizaCreadoEn() {
        // 1. Arrange
        Usuario usuarioEnDB = entityManager.persistAndFlush(Usuario.builder()
                .nombre("Carlos")
                .correo("carlos@test.com")
                .password("pass-789")
                .rol("USER")
                .build());
        
        LocalDateTime fechaCreacionOriginal = usuarioEnDB.getCreadoEn();
        assertThat(fechaCreacionOriginal).isNotNull(); // Pre-verificación

        // 2. Act
        Usuario usuarioParaActualizar = usuarioEncontrado.get();
        usuarioParaActualizar.setNombre("Carlos Actualizado");
        usuarioParaActualizar.setRol("MODERATOR");
        usuarioRepository.saveAndFlush(usuarioParaActualizar); // Usamos saveAndFlush para forzar

        // 3. Assert
        Usuario usuarioActualizado = usuarioRepository.findById(usuarioEnDB.getId()).get();
        assertThat(usuarioActualizado.getNombre()).isEqualTo("Carlos Actualizado");
        assertThat(usuarioActualizado.getRol()).isEqualTo("MODERATOR");
        
        // Verificamos que 'creadoEn' NO cambió (updatable = false)
        assertThat(usuarioActualizado.getCreadoEn()).isEqualTo(fechaCreacionOriginal);
    }
    
    // --- Pruebas del Método Personalizado ---

    @Test
    public void cuandoFindByCorreo_conCorreoExistente_entoncesRetornaUsuario() {
        // 1. Arrange
        String correoBuscado = "buscame@test.com";
        Usuario usuario = Usuario.builder()
                .nombre("Usuario Encontrado")
                .correo(correoBuscado)
                .password("pass")
                .rol("USER")
                .build();
        entityManager.persistAndFlush(usuario);

        // 2. Act
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByCorreo(correoBuscado);

        // 3. Assert
        assertThat(usuarioEncontrado).isPresent();
        assertThat(usuarioEncontrado.get().getNombre()).isEqualTo("Usuario Encontrado");
    }

    @Test
    public void cuandoFindByCorreo_conCorreoNoExistente_entoncesRetornaOptionalVacio() {
        // 1. Arrange
        // No persistimos ningún usuario con el correo "noexiste@test.com"

        // 2. Act
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByCorreo("noexiste@test.com");

        // 3. Assert
        assertThat(usuarioEncontrado).isNotPresent();
    }

    // --- Prueba de Restricción (Constraint) ---

    @Test
    public void cuandoGuardaCorreoDuplicado_entoncesLanzaDataIntegrityViolationException() {
        // 1. Arrange
        // Guardamos el primer usuario
        Usuario usuario1 = Usuario.builder()
                .nombre("Usuario Uno")
                .correo("correo.duplicado@test.com")
                .password("pass1")
                .rol("USER")
                .build();
        entityManager.persistAndFlush(usuario1);

        // Preparamos el segundo usuario con el MISMO correo
        Usuario usuario2 = Usuario.builder()
                .nombre("Usuario Dos")
                .correo("correo.duplicado@test.com") // Correo repetido
                .password("pass2")
                .rol("USER")
                .build();

        // 2. Act & 3. Assert
        // Verificamos que la operación de guardar y flushear lanza la excepción
        assertThatThrownBy(() -> {
            usuarioRepository.save(usuario2);
            entityManager.flush(); // La excepción se lanza al hacer flush (commit a la BD)
        })
        .isInstanceOf(DataIntegrityViolationException.class); // Verificamos que es la excepción correcta
    }
}