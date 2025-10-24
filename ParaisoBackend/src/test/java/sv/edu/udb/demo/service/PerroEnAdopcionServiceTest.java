package sv.edu.udb.demo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.udb.demo.dto.PerroCreateDTO;
import sv.edu.udb.demo.dto.PerroUpdateDTO;
import sv.edu.udb.demo.model.PerroEnAdopcion;
import sv.edu.udb.demo.repository.PerroEnAdopcionRepository;

import java.util.List;
import java.util.Optional;

// Importaciones estáticas para Mockito y AssertJ
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PerroEnAdopcionServiceTest {

    @Mock
    private PerroEnAdopcionRepository repo;

    @InjectMocks
    private PerroEnAdopcionService perroService;

    @Test
    void cuandoFindAll_entoncesRetornaLista() {
        // 1. Arrange
        PerroEnAdopcion p1 = PerroEnAdopcion.builder().id(1).nombre("Firulais").build();
        PerroEnAdopcion p2 = PerroEnAdopcion.builder().id(2).nombre("Max").build();
        when(repo.findAll()).thenReturn(List.of(p1, p2));

        // 2. Act
        List<PerroEnAdopcion> resultado = perroService.findAll();

        // 3. Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.size()).isEqualTo(2);
        verify(repo, times(1)).findAll();
    }

    @Test
    void cuandoFindById_conIdExistente_entoncesRetornaPerro() {
        // 1. Arrange
        Integer id = 1;
        PerroEnAdopcion perroFalso = PerroEnAdopcion.builder().id(id).nombre("Firulais").build();
        when(repo.findById(id)).thenReturn(Optional.of(perroFalso));

        // 2. Act
        PerroEnAdopcion resultado = perroService.findById(id);

        // 3. Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getNombre()).isEqualTo("Firulais");
        verify(repo, times(1)).findById(id);
    }

    @Test
    void cuandoFindById_conIdNoExistente_entoncesLanzaExcepcion() {
        // 1. Arrange
        Integer id = 99;
        when(repo.findById(id)).thenReturn(Optional.empty());

        // 2. Act & 3. Assert
        assertThatThrownBy(() -> perroService.findById(id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Perro no existe");
        verify(repo, times(1)).findById(id);
    }

    @Test
    void cuandoCreate_entoncesTransformaDTOYGuarda() {
        // 1. Arrange
        PerroCreateDTO dto = new PerroCreateDTO("Chispita", "Chihuahua", 3, "url.jpg", "Pequeño");

        // Usamos ArgumentCaptor para verificar que el objeto construido
        // a partir del DTO es correcto ANTES de guardarlo.
        ArgumentCaptor<PerroEnAdopcion> perroCaptor = ArgumentCaptor.forClass(PerroEnAdopcion.class);
        
        // Simulamos que el 'save' le asigna un ID
        PerroEnAdopcion perroGuardado = PerroEnAdopcion.builder()
                .id(1).nombre(dto.nombre()).raza(dto.raza()).edad(dto.edad())
                .img(dto.img()).descr(dto.descr()).build();
        
        when(repo.save(perroCaptor.capture())).thenReturn(perroGuardado);

        // 2. Act
        PerroEnAdopcion resultado = perroService.create(dto);

        // 3. Assert
        // A) Verificar el objeto retornado (post-guardado)
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1);
        assertThat(resultado.getNombre()).isEqualTo("Chispita");

        // B) Verificar el objeto capturado (pre-guardado)
        PerroEnAdopcion perroAntesDeGuardar = perroCaptor.getValue();
        assertThat(perroAntesDeGuardar.getId()).isNull(); // No tenía ID aún
        assertThat(perroAntesDeGuardar.getNombre()).isEqualTo("Chispita");
        assertThat(perroAntesDeGuardar.getRaza()).isEqualTo("Chihuahua");
    }

    @Test
    void cuandoUpdate_conIdExistenteYDatosParciales_entoncesActualizaParcialmente() {
        // 1. Arrange
        Integer id = 1;
        // El DTO solo actualiza nombre y edad. Raza, img y descr son null.
        PerroUpdateDTO dto = new PerroUpdateDTO("Max Actualizado", null, 6, null, null);

        // Estado del perro en la BD
        PerroEnAdopcion perroEnDB = PerroEnAdopcion.builder()
                .id(id)
                .nombre("Max Original")
                .raza("Pastor Alemán")
                .edad(5)
                .img("original.jpg")
                .descr("Original descr")
                .build();
        
        // Mock 1: findById lo encuentra
        when(repo.findById(id)).thenReturn(Optional.of(perroEnDB));
        // Mock 2: save retorna el objeto que se le pasa
        when(repo.save(any(PerroEnAdopcion.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // 2. Act
        PerroEnAdopcion resultado = perroService.update(id, dto);

        // 3. Assert
        assertThat(resultado).isNotNull();
        
        // Campos que SÍ debían cambiar
        assertThat(resultado.getNombre()).isEqualTo("Max Actualizado");
        assertThat(resultado.getEdad()).isEqualTo(6);
        
        // Campos que NO debían cambiar (porque DTO era null)
        assertThat(resultado.getRaza()).isEqualTo("Pastor Alemán");
        assertThat(resultado.getImg()).isEqualTo("original.jpg");
        assertThat(resultado.getDescr()).isEqualTo("Original descr");
        
        verify(repo, times(1)).findById(id);
        verify(repo, times(1)).save(perroEnDB); // Verifica que se guardó el mismo objeto modificado
    }

    @Test
    void cuandoUpdate_conIdNoExistente_entoncesLanzaExcepcion() {
        // 1. Arrange
        Integer id = 99;
        PerroUpdateDTO dto = new PerroUpdateDTO("Test", null, 0, null, null);
        
        when(repo.findById(id)).thenReturn(Optional.empty());

        // 2. Act & 3. Assert
        assertThatThrownBy(() -> perroService.update(id, dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Perro no existe");
        
        verify(repo, times(1)).findById(id);
        verify(repo, never()).save(any(PerroEnAdopcion.class));
    }

    @Test
    void cuandoDelete_conIdExistente_entoncesEliminaExitosamente() {
        // 1. Arrange
        Integer id = 1;
        when(repo.existsById(id)).thenReturn(true);
        doNothing().when(repo).deleteById(id); // Para métodos void

        // 2. Act
        perroService.delete(id);

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
        assertThatThrownBy(() -> perroService.delete(id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Perro no existe");

        verify(repo, times(1)).existsById(id);
        verify(repo, never()).deleteById(id);
    }
}