package sv.edu.udb.demo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.udb.demo.dto.AlcanciaCreateDTO;
import sv.edu.udb.demo.dto.AlcanciaUpdateDTO;
import sv.edu.udb.demo.model.Alcancia;
import sv.edu.udb.demo.repository.AlcanciaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

// Importaciones estáticas para Mockito y AssertJ
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

/**
 * @ExtendWith(MockitoExtension.class)
 * Inicializa los mocks y la clase bajo prueba.
 */
@ExtendWith(MockitoExtension.class)
public class AlcanciaServiceTest {

    /**
     * @Mock crea una simulación (un objeto falso) del repositorio.
     * Este mock NO se conectará a ninguna base de datos.
     */
    @Mock
    private AlcanciaRepository repo;

    /**
     * @InjectMocks crea una instancia real de AlcanciaService
     * e intenta inyectar CUALQUIER @Mock definido en esta clase (en este caso, 'repo').
     */
    @InjectMocks
    private AlcanciaService alcanciaService;

    @Test
    void cuandoFindAll_entoncesRetornaListaDeAlcancias() {
        // 1. Arrange (Preparar)
        // Creamos datos falsos
        Alcancia a1 = Alcancia.builder().id(1).descr("Ahorro 1").build();
        Alcancia a2 = Alcancia.builder().id(2).descr("Ahorro 2").build();
        List<Alcancia> listaFalsa = List.of(a1, a2);

        // Configuramos el mock: "CUANDO llamen a repo.findAll(), ENTONCES retorna listaFalsa"
        when(repo.findAll()).thenReturn(listaFalsa);

        // 2. Act (Actuar)
        List<Alcancia> resultado = alcanciaService.findAll();

        // 3. Assert (Verificar)
        assertThat(resultado).isNotNull();
        assertThat(resultado.size()).isEqualTo(2);
        // Verificamos que el método 'findAll' del mock fue llamado exactamente 1 vez
        verify(repo, times(1)).findAll();
    }

    @Test
    void cuandoFindById_conIdExistente_entoncesRetornaAlcancia() {
        // 1. Arrange
        Integer id = 1;
        Alcancia alcanciaFalsa = Alcancia.builder()
                .id(id).descr("Test").precioMeta(new BigDecimal("100")).build();
        
        // "CUANDO llamen a repo.findById(1), ENTONCES retorna un Optional de nuestra alcanciaFalsa"
        when(repo.findById(id)).thenReturn(Optional.of(alcanciaFalsa));

        // 2. Act
        Alcancia resultado = alcanciaService.findById(id);

        // 3. Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(id);
        assertThat(resultado.getDescr()).isEqualTo("Test");
        verify(repo, times(1)).findById(id);
    }

    @Test
    void cuandoFindById_conIdNoExistente_entoncesLanzaExcepcion() {
        // 1. Arrange
        Integer id = 99;
        
        // "CUANDO llamen a repo.findById(99), ENTONCES retorna un Optional vacío"
        when(repo.findById(id)).thenReturn(Optional.empty());

        // 2. Act & 3. Assert
        // Verificamos que llamar al método lanza la excepción esperada
        assertThatThrownBy(() -> alcanciaService.findById(id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Alcancía no existe");
        
        verify(repo, times(1)).findById(id);
    }

    @Test
    void cuandoCreate_entoncesGuardaYRetornaAlcanciaConPrecioActualCero() {
        // 1. Arrange
        AlcanciaCreateDTO dto = new AlcanciaCreateDTO("Nuevo Ahorro", new BigDecimal("500.00"));
        
        // Esto es lo que esperamos que el servicio retorne (con ID y precioActual=0)
        Alcancia alcanciaGuardada = Alcancia.builder()
                .id(1)
                .descr("Nuevo Ahorro")
                .precioMeta(new BigDecimal("500.00"))
                .precioActual(BigDecimal.ZERO) // La lógica clave a probar
                .build();

        // "CUANDO llamen a repo.save(CUALQUIER objeto Alcancia), ENTONCES retorna nuestra alcanciaGuardada"
        when(repo.save(any(Alcancia.class))).thenReturn(alcanciaGuardada);

        // 2. Act
        Alcancia resultado = alcanciaService.create(dto);

        // 3. Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1);
        assertThat(resultado.getDescr()).isEqualTo("Nuevo Ahorro");
        // ¡La aserción más importante! Verificamos la lógica de negocio del servicio
        assertThat(resultado.getPrecioActual().compareTo(BigDecimal.ZERO)).isEqualTo(0);
        
        verify(repo, times(1)).save(any(Alcancia.class));
    }

    @Test
    void cuandoUpdate_conIdExistente_entoncesRetornaAlcanciaActualizada() {
        // 1. Arrange
        Integer id = 1;
        // DTO solo actualiza la descripción, precioMeta es null
        AlcanciaUpdateDTO dto = new AlcanciaUpdateDTO("Descripción Actualizada", null);

        // Objeto que simulamos ya existe en la BD
        Alcancia alcanciaExistente = Alcancia.builder()
                .id(id)
                .descr("Descripción Original")
                .precioMeta(new BigDecimal("100.00"))
                .precioActual(new BigDecimal("10.00")) // Precio actual no debe cambiar
                .build();
        
        // Mock 1: El findById debe encontrar la alcancía
        when(repo.findById(id)).thenReturn(Optional.of(alcanciaExistente));
        
        // Mock 2: El save debe retornar el objeto que se le pasa
        when(repo.save(any(Alcancia.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // 2. Act
        Alcancia resultado = alcanciaService.update(id, dto);

        // 3. Assert
        assertThat(resultado).isNotNull();
        // Verificamos la lógica de actualización condicional
        assertThat(resultado.getDescr()).isEqualTo("Descripción Actualizada"); // Se actualizó
        assertThat(resultado.getPrecioMeta().compareTo(new BigDecimal("100.00"))).isEqualTo(0); // No se actualizó (DTO era null)
        assertThat(resultado.getPrecioActual().compareTo(new BigDecimal("10.00"))).isEqualTo(0); // No se tocó

        verify(repo, times(1)).findById(id);
        verify(repo, times(1)).save(any(Alcancia.class));
    }

    @Test
    void cuandoUpdate_conIdNoExistente_entoncesLanzaExcepcion() {
        // 1. Arrange
        Integer id = 99;
        AlcanciaUpdateDTO dto = new AlcanciaUpdateDTO("Test", new BigDecimal("100"));
        
        // El servicio primero llama a findById, simulamos que falla
        when(repo.findById(id)).thenReturn(Optional.empty());

        // 2. Act & 3. Assert
        assertThatThrownBy(() -> alcanciaService.update(id, dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Alcancía no existe");
        
        verify(repo, times(1)).findById(id);
        // Verificamos que 'save' NUNCA fue llamado
        verify(repo, never()).save(any(Alcancia.class));
    }

    @Test
    void cuandoDelete_conIdExistente_entoncesEliminaExitosamente() {
        // 1. Arrange
        Integer id = 1;
        // "CUANDO llamen a repo.existsById(1), ENTONCES retorna true"
        when(repo.existsById(id)).thenReturn(true);
        // Para métodos 'void' como deleteById, podemos usar doNothing() (aunque a veces es opcional)
        doNothing().when(repo).deleteById(id);

        // 2. Act
        // Llamamos al método, no esperamos que retorne nada
        alcanciaService.delete(id);

        // 3. Assert
        // Verificamos que los métodos correctos del mock fueron llamados
        verify(repo, times(1)).existsById(id);
        verify(repo, times(1)).deleteById(id);
    }

    @Test
    void cuandoDelete_conIdNoExistente_entoncesLanzaExcepcion() {
        // 1. Arrange
        Integer id = 99;
        // "CUANDO llamen a repo.existsById(99), ENTONCES retorna false"
        when(repo.existsById(id)).thenReturn(false);

        // 2. Act & 3. Assert
        assertThatThrownBy(() -> alcanciaService.delete(id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Alcancía no existe");

        verify(repo, times(1)).existsById(id);
        // Verificamos que 'deleteById' NUNCA fue llamado
        verify(repo, never()).deleteById(id);
    }
}