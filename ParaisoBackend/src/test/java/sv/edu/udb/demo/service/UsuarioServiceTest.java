package sv.edu.udb.demo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import sv.edu.udb.demo.dto.PasswordChangeDTO;
import sv.edu.udb.demo.dto.UsuarioCreateDTO;
import sv.edu.udb.demo.dto.UsuarioUpdateDTO;
import sv.edu.udb.demo.model.Usuario;
import sv.edu.udb.demo.repository.UsuarioRepository;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repo;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UsuarioService usuarioService;

    // --- Tests de findById ---

    @Test
    void cuandoFindById_conIdExistente_entoncesRetornaUsuario() {
        // 1. Arrange
        Integer id = 1;
        Usuario usuarioFalso = Usuario.builder().id(id).nombre("Test").correo("test@test.com").build();
        when(repo.findById(id)).thenReturn(Optional.of(usuarioFalso));

        // 2. Act
        Usuario resultado = usuarioService.findById(id);

        // 3. Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(id);
        verify(repo, times(1)).findById(id);
    }

    @Test
    void cuandoFindById_conIdNoExistente_entoncesLanzaExcepcion() {
        // 1. Arrange
        Integer id = 99;
        when(repo.findById(id)).thenReturn(Optional.empty());

        // 2. Act & 3. Assert
        assertThatThrownBy(() -> usuarioService.findById(id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Usuario no existe");
    }

    // --- Tests de create ---

    @Test
    void cuandoCreate_conDatosValidosYRolNull_entoncesGuardaConRolUSER() {
        // 1. Arrange
        UsuarioCreateDTO dto = new UsuarioCreateDTO("Juan", "juan@test.com", "pass123", null);
        String passHasheada = "HASHED_PASS_123";

        // Mock: Correo no existe
        when(repo.findByCorreo(dto.correo())).thenReturn(Optional.empty());
        // Mock: El encoder hashea la contraseña
        when(encoder.encode(dto.password())).thenReturn(passHasheada);
        
        // Capturamos el usuario que se pasa al 'save'
        ArgumentCaptor<Usuario> usuarioCaptor = ArgumentCaptor.forClass(Usuario.class);
        
        // Mock: El save retorna el usuario con un ID
        Usuario usuarioGuardado = Usuario.builder().id(1).nombre(dto.nombre()).build();
        when(repo.save(usuarioCaptor.capture())).thenReturn(usuarioGuardado);

        // 2. Act
        Usuario resultado = usuarioService.create(dto);

        // 3. Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1);
        
        // Verificamos el objeto capturado (antes de guardar)
        Usuario usuarioAntesDeGuardar = usuarioCaptor.getValue();
        assertThat(usuarioAntesDeGuardar.getPassword()).isEqualTo(passHasheada); // ¡Clave hasheada!
        assertThat(usuarioAntesDeGuardar.getRol()).isEqualTo("USER"); // ¡Rol por defecto!
    }

    @Test
    void cuandoCreate_conDatosValidosYRolEspecifico_entoncesGuardaRolEnMayusculas() {
        // 1. Arrange
        UsuarioCreateDTO dto = new UsuarioCreateDTO("Admin", "admin@test.com", "pass123", "admin");
        
        when(repo.findByCorreo(dto.correo())).thenReturn(Optional.empty());
        when(encoder.encode(anyString())).thenReturn("hash");
        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        when(repo.save(captor.capture())).thenReturn(new Usuario());

        // 2. Act
        usuarioService.create(dto);

        // 3. Assert
        assertThat(captor.getValue().getRol()).isEqualTo("ADMIN"); // Lógica de mayúsculas
    }

    @Test
    void cuandoCreate_conCorreoExistente_entoncesLanzaExcepcion() {
        // 1. Arrange
        UsuarioCreateDTO dto = new UsuarioCreateDTO("Juan", "juan@test.com", "pass123", null);
        
        // Mock: El correo SÍ existe
        when(repo.findByCorreo(dto.correo())).thenReturn(Optional.of(new Usuario()));

        // 2. Act & 3. Assert
        assertThatThrownBy(() -> usuarioService.create(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Correo ya está en uso");
        
        // Verificamos que NUNCA se intentó hashear o guardar
        verify(encoder, never()).encode(anyString());
        verify(repo, never()).save(any(Usuario.class));
    }

    // --- Tests de update ---
    
    @Test
    void cuandoUpdate_conDatosValidos_entoncesActualizaParcialmente() {
        // 1. Arrange
        Integer id = 1;
        // DTO solo actualiza nombre y rol (correo es null)
        UsuarioUpdateDTO dto = new UsuarioUpdateDTO("Nombre Nuevo", null, "moderator");

        Usuario usuarioEnDB = Usuario.builder()
                .id(id).nombre("Nombre Viejo").correo("viejo@test.com").rol("USER").build();

        when(repo.findById(id)).thenReturn(Optional.of(usuarioEnDB));
        when(repo.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0)); // Retorna el objeto guardado

        // 2. Act
        Usuario resultado = usuarioService.update(id, dto);

        // 3. Assert
        assertThat(resultado.getNombre()).isEqualTo("Nombre Nuevo");
        assertThat(resultado.getRol()).isEqualTo("MODERATOR"); // Se actualizó y puso en mayúsculas
        assertThat(resultado.getCorreo()).isEqualTo("viejo@test.com"); // No cambió

        // Verificamos que NO se buscó el correo (porque era null en el DTO)
        verify(repo, never()).findByCorreo(anyString());
        verify(repo, times(1)).save(usuarioEnDB);
    }
    
    @Test
    void cuandoUpdate_cambiandoCorreoValido_entoncesActualizaCorreo() {
        // 1. Arrange
        Integer id = 1;
        UsuarioUpdateDTO dto = new UsuarioUpdateDTO(null, "nuevo@test.com", null);
        Usuario usuarioEnDB = Usuario.builder()
                .id(id).nombre("Test").correo("viejo@test.com").rol("USER").build();
        
        when(repo.findById(id)).thenReturn(Optional.of(usuarioEnDB));
        // Mock: El nuevo correo NO está en uso
        when(repo.findByCorreo(dto.correo())).thenReturn(Optional.empty());
        when(repo.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        // 2. Act
        Usuario resultado = usuarioService.update(id, dto);

        // 3. Assert
        assertThat(resultado.getCorreo()).isEqualTo("nuevo@test.com");
        verify(repo, times(1)).findByCorreo("nuevo@test.com"); // Se hizo la validación
        verify(repo, times(1)).save(usuarioEnDB);
    }

    @Test
    void cuandoUpdate_cambiandoCorreoAunoExistente_entoncesLanzaExcepcion() {
        // 1. Arrange
        Integer id = 1;
        String correoEnUso = "enuso@test.com";
        UsuarioUpdateDTO dto = new UsuarioUpdateDTO(null, correoEnUso, null);
        
        Usuario usuarioEnDB = Usuario.builder()
                .id(id).nombre("Test").correo("viejo@test.com").rol("USER").build();
        
        when(repo.findById(id)).thenReturn(Optional.of(usuarioEnDB));
        // Mock: El nuevo correo SÍ está en uso por OTRO usuario
        when(repo.findByCorreo(correoEnUso)).thenReturn(Optional.of(Usuario.builder().id(2).build()));

        // 2. Act & 3. Assert
        assertThatThrownBy(() -> usuarioService.update(id, dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Correo ya está en uso");
        
        verify(repo, never()).save(any(Usuario.class)); // No se guardó
    }

    // --- Test de changePassword ---
    
    @Test
    void cuandoChangePassword_entoncesHasheaYGuarda() {
        // 1. Arrange
        Integer id = 1;
        PasswordChangeDTO dto = new PasswordChangeDTO("nuevaClaveSecreta");
        String passHasheada = "NUEVO_HASH_456";

        Usuario usuarioEnDB = Usuario.builder()
                .id(id).nombre("Test").password("viejoHash").build();

        when(repo.findById(id)).thenReturn(Optional.of(usuarioEnDB));
        when(encoder.encode(dto.password())).thenReturn(passHasheada);
        
        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        when(repo.save(captor.capture())).thenReturn(usuarioEnDB);

        // 2. Act
        usuarioService.changePassword(id, dto);

        // 3. Assert
        verify(repo, times(1)).findById(id);
        verify(encoder, times(1)).encode("nuevaClaveSecreta");
        verify(repo, times(1)).save(any(Usuario.class));
        
        // Verificamos que la contraseña en el objeto guardado es la hasheada
        assertThat(captor.getValue().getPassword()).isEqualTo(passHasheada);
    }

    // --- Test de delete ---

    @Test
    void cuandoDelete_conIdExistente_entoncesElimina() {
        // 1. Arrange
        Integer id = 1;
        when(repo.existsById(id)).thenReturn(true);
        doNothing().when(repo).deleteById(id);

        // 2. Act
        usuarioService.delete(id);

        // 3. Assert
        verify(repo, times(1)).existsById(id);
        verify(repo, times(1)).deleteById(id);
    }
    
    @Test
    void cuandoDelete_conIdNoExistente_entoncesLanzaExcepcion() {
        // 1. Arrange
        Integer id = 99;
        when(repo.existsById(id)).thenReturn(false);

        // 2. Act & 3. Assert
        assertThatThrownBy(() -> usuarioService.delete(id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Usuario no existe");

        verify(repo, never()).deleteById(id);
    }
}