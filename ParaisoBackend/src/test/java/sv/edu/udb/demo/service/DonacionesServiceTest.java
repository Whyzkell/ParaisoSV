package sv.edu.udb.demo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.udb.demo.dto.DonacionesCreateDTO;
import sv.edu.udb.demo.model.Alcancia;
import sv.edu.udb.demo.model.Donaciones;
import sv.edu.udb.demo.model.Usuario;
import sv.edu.udb.demo.repository.AlcanciaRepository;
import sv.edu.udb.demo.repository.DonacionesRepository;
import sv.edu.udb.demo.repository.UsuarioRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class DonacionesServiceTest {

    @Mock
    private DonacionesRepository donacionesRepo;
    @Mock
    private AlcanciaRepository alcanciaRepo;
    @Mock
    private UsuarioRepository usuarioRepo;

    @InjectMocks
    private DonacionesService donacionesService;

    @Test
    void cuandoCrearDonacion_conDatosValidos_entoncesGuardaDonacionYActualizaAlcancia() {
        // 1. Arrange (Preparar)
        Integer idUsuario = 1;
        Integer idAlcancia = 10;
        BigDecimal montoDonacion = new BigDecimal("50.00");
        BigDecimal montoPrevioAlcancia = new BigDecimal("100.00");
        // El resultado esperado de la suma (100.00 + 50.00)
        BigDecimal montoEsperadoAlcancia = new BigDecimal("150.00");

        DonacionesCreateDTO dto = new DonacionesCreateDTO(idAlcancia, montoDonacion);

        // Creamos los objetos falsos que retornarán los mocks
        Usuario usuarioFalso = Usuario.builder().id(idUsuario).nombre("Donador").build();
        Alcancia alcanciaFalsa = Alcancia.builder()
                .id(idAlcancia)
                .descr("Ahorro")
                .precioActual(montoPrevioAlcancia) // Estado inicial
                .build();

        // Creamos el objeto Donacion que esperamos que 'save' retorne
        Donaciones donacionGuardadaFalsa = Donaciones.builder()
                .id(123) // Un ID autogenerado
                .usuario(usuarioFalso)
                .alcancia(alcanciaFalsa)
                .cantidadDonada(montoDonacion)
                .build();

        // Configuración de Mocks (when... thenReturn)
        when(usuarioRepo.findById(idUsuario)).thenReturn(Optional.of(usuarioFalso));
        when(alcanciaRepo.findById(idAlcancia)).thenReturn(Optional.of(alcanciaFalsa));
        when(donacionesRepo.save(any(Donaciones.class))).thenReturn(donacionGuardadaFalsa);
        // No necesitamos mockear alcanciaRepo.save(), ya que la lógica principal
        // es la mutación del objeto 'alcanciaFalsa' en memoria.

        // 2. Act (Actuar)
        Donaciones resultado = donacionesService.crearDonacion(idUsuario, dto);

        // 3. Assert (Verificar)

        // A) Verificar el objeto retornado
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(123);
        assertThat(resultado.getUsuario().getId()).isEqualTo(idUsuario);
        assertThat(resultado.getCantidadDonada().compareTo(montoDonacion)).isEqualTo(0);

        // B) Verificar que el 'precioActual' de la alcancía se modificó ANTES de guardar
        // Comprobamos el estado del objeto 'alcanciaFalsa' (que fue mutado por el servicio)
        assertThat(alcanciaFalsa.getPrecioActual().compareTo(montoEsperadoAlcancia)).isEqualTo(0);

        // C) Verificar que los 'save' fueron llamados
        verify(donacionesRepo, times(1)).save(any(Donaciones.class));
        // Verificamos que se llamó a guardar la alcancía (con su estado mutado)
        verify(alcanciaRepo, times(1)).save(alcanciaFalsa);
    }
    
    @Test
    void cuandoCrearDonacion_conUsuarioInvalido_entoncesLanzaExcepcion() {
        // 1. Arrange
        Integer idUsuario = 99; // ID no existente
        DonacionesCreateDTO dto = new DonacionesCreateDTO(1, new BigDecimal("10.00"));
        
        when(usuarioRepo.findById(idUsuario)).thenReturn(Optional.empty());

        // 2. Act & 3. Assert
        assertThatThrownBy(() -> donacionesService.crearDonacion(idUsuario, dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Usuario no existe");

        // Verificamos que la ejecución se detuvo y no se llamó a otros repos
        verify(alcanciaRepo, never()).findById(anyInt());
        verify(donacionesRepo, never()).save(any(Donaciones.class));
        verify(alcanciaRepo, never()).save(any(Alcancia.class));
    }

    @Test
    void cuandoCrearDonacion_conAlcanciaInvalida_entoncesLanzaExcepcion() {
        // 1. Arrange
        Integer idUsuario = 1;
        Integer idAlcancia = 99; // ID no existente
        DonacionesCreateDTO dto = new DonacionesCreateDTO(idAlcancia, new BigDecimal("10.00"));
        
        // El usuario sí se encuentra
        when(usuarioRepo.findById(idUsuario)).thenReturn(Optional.of(Usuario.builder().id(idUsuario).build()));
        // Pero la alcancía no
        when(alcanciaRepo.findById(idAlcancia)).thenReturn(Optional.empty());

        // 2. Act & 3. Assert
        assertThatThrownBy(() -> donacionesService.crearDonacion(idUsuario, dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Alcancía no existe");
        
        // Verificamos que no se intentó guardar nada
        verify(donacionesRepo, never()).save(any(Donaciones.class));
        verify(alcanciaRepo, never()).save(any(Alcancia.class));
    }

    @Test
    void cuandoCrearDonacion_conMontoCero_entoncesLanzaExcepcion() {
        // 1. Arrange
        Integer idUsuario = 1;
        Integer idAlcancia = 10;
        // DTO con monto 0
        DonacionesCreateDTO dto = new DonacionesCreateDTO(idAlcancia, BigDecimal.ZERO);

        // Los repositorios encuentran las entidades
        when(usuarioRepo.findById(idUsuario)).thenReturn(Optional.of(Usuario.builder().id(idUsuario).build()));
        when(alcanciaRepo.findById(idAlcancia)).thenReturn(Optional.of(Alcancia.builder().id(idAlcancia).build()));

        // 2. Act & 3. Assert
        assertThatThrownBy(() -> donacionesService.crearDonacion(idUsuario, dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("La cantidad debe ser mayor a 0");

        // Verificamos que, aunque se encontraron las entidades, no se guardó nada
        verify(donacionesRepo, never()).save(any(Donaciones.class));
        verify(alcanciaRepo, never()).save(any(Alcancia.class));
    }
    
    @Test
    void cuandoCrearDonacion_conMontoNegativo_entoncesLanzaExcepcion() {
        // 1. Arrange
        Integer idUsuario = 1;
        Integer idAlcancia = 10;
        // DTO con monto negativo
        DonacionesCreateDTO dto = new DonacionesCreateDTO(idAlcancia, new BigDecimal("-50.00"));

        when(usuarioRepo.findById(idUsuario)).thenReturn(Optional.of(Usuario.builder().id(idUsuario).build()));
        when(alcanciaRepo.findById(idAlcancia)).thenReturn(Optional.of(Alcancia.builder().id(idAlcancia).build()));

        // 2. Act & 3. Assert
        assertThatThrownBy(() -> donacionesService.crearDonacion(idUsuario, dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("La cantidad debe ser mayor a 0");
    }
}