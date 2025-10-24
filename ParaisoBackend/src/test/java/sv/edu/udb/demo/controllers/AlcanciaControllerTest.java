package sv.edu.udb.demo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sv.edu.udb.demo.controller.AlcanciaController;
import sv.edu.udb.demo.dto.AlcanciaCreateDTO;
import sv.edu.udb.demo.dto.AlcanciaUpdateDTO;
import sv.edu.udb.demo.model.Alcancia;
import sv.edu.udb.demo.model.Donaciones;
import sv.edu.udb.demo.model.Usuario;
import sv.edu.udb.demo.repository.DonacionesRepository;
import sv.edu.udb.demo.service.AlcanciaService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

// Importaciones estáticas para MockMvc y Mockito
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*; // Para 'is()' y 'hasSize()'

/**
 * @WebMvcTest(AlcanciaController.class)
 * Carga solo el contexto necesario para probar el AlcanciaController.
 * NO carga los @Service ni @Repository reales.
 */
@WebMvcTest(AlcanciaController.class)
public class AlcanciaControllerTest {

    @Autowired
    private MockMvc mvc; // El cliente para simular peticiones HTTP

    @Autowired
    private ObjectMapper objectMapper; // Para convertir DTOs a JSON

    /**
     * @MockBean simula las dependencias del controlador.
     * Son necesarios porque el contexto real del servicio/repositorio no se carga.
     */
    @MockBean
    private AlcanciaService service;

    @MockBean
    private DonacionesRepository donacionesRepo;

    // --- Datos de prueba reutilizables ---
    private Alcancia alcancia1;
    private Usuario user1;
    private Donaciones donacion1;
    private Donaciones donacionDeOtraAlcancia;

    @BeforeEach
    void setUp() {
        // Objeto base para las pruebas
        alcancia1 = Alcancia.builder()
                .id(1)
                .descr("Ahorro para Viaje")
                .precioMeta(new BigDecimal("1000.00"))
                .precioActual(new BigDecimal("150.00"))
                .build();
        
        user1 = Usuario.builder().id(10).nombre("Donador Uno").build();

        // Donación para alcancia1
        donacion1 = Donaciones.builder()
                .id(100)
                .alcancia(alcancia1)
                .usuario(user1)
                .cantidadDonada(new BigDecimal("50.00"))
                .fecha(LocalDateTime.now())
                .build();

        // Donación para OTRA alcancía (id=2), para probar filtros
        Alcancia alcancia2 = Alcancia.builder().id(2).build();
        donacionDeOtraAlcancia = Donaciones.builder()
                .id(101)
                .alcancia(alcancia2) // Alcancía diferente
                .usuario(user1)
                .cantidadDonada(new BigDecimal("10.00"))
                .build();
    }

    // --- Pruebas CRUD ---

    @Test
    void cuandoListar_entoncesRetorna200OK_yListaDeAlcancias() throws Exception {
        // Arrange
        when(service.findAll()).thenReturn(List.of(alcancia1));

        // Act & Assert
        mvc.perform(get("/api/alcancias"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1))) // Verifica que la lista tiene 1 elemento
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].descr", is("Ahorro para Viaje")));
    }

    @Test
    void cuandoPorId_conIdExistente_entoncesRetorna200OK_yAlcancia() throws Exception {
        // Arrange
        when(service.findById(1)).thenReturn(alcancia1);

        // Act & Assert
        mvc.perform(get("/api/alcancias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.precioMeta", is(1000.00)));
    }

    @Test
    void cuandoPorId_conIdNoExistente_entoncesRetorna404NotFound() throws Exception {
        // Arrange
        // Simulamos la excepción que el service lanza y el controller atrapa
        when(service.findById(99)).thenThrow(new IllegalArgumentException("Alcancía no existe"));

        // Act & Assert
        mvc.perform(get("/api/alcancias/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void cuandoCrear_conDTOValido_entoncesRetorna201Created_yAlcanciaCreada() throws Exception {
        // Arrange
        AlcanciaCreateDTO dto = new AlcanciaCreateDTO("Nueva", new BigDecimal("500.00"));
        
        // El servicio procesa el DTO y devuelve la entidad completa
        Alcancia alcanciaCreada = Alcancia.builder()
                .id(2).descr("Nueva").precioMeta(new BigDecimal("500.00")).precioActual(BigDecimal.ZERO).build();
        
        when(service.create(any(AlcanciaCreateDTO.class))).thenReturn(alcanciaCreada);

        // Act & Assert
        mvc.perform(post("/api/alcancias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))) // Serializa el DTO a JSON
                .andExpect(status().isCreated()) // 201
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.precioActual", is(0))); // Verifica lógica del servicio
    }

    @Test
    void cuandoCrear_conDTOInvalido_entoncesRetorna400BadRequest() throws Exception {
        // Arrange
        // DTO inválido (precioMeta es @NotNull y @Positive)
        AlcanciaCreateDTO dtoInvalido = new AlcanciaCreateDTO("Inválida", null);

        // Act & Assert
        mvc.perform(post("/api/alcancias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoInvalido)))
                .andExpect(status().isBadRequest()); // 400

        // Verificamos que el servicio NUNCA fue llamado, la validación actuó antes
        verify(service, never()).create(any(AlcanciaCreateDTO.class));
    }

    @Test
    void cuandoActualizar_conIdExistente_entoncesRetorna200OK_yAlcanciaActualizada() throws Exception {
        // Arrange
        AlcanciaUpdateDTO dto = new AlcanciaUpdateDTO("Descripción Actualizada", null);
        
        // El servicio aplica la actualización parcial
        alcancia1.setDescr("Descripción Actualizada");
        
        when(service.update(eq(1), any(AlcanciaUpdateDTO.class))).thenReturn(alcancia1);

        // Act & Assert
        mvc.perform(put("/api/alcancias/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1S)))
                .andExpect(jsonPath("$.descr", is("Descripción Actualizada")));
    }
    
    @Test
    void cuandoActualizar_conIdNoExistente_entoncesRetorna404NotFound() throws Exception {
        // Arrange
        AlcanciaUpdateDTO dto = new AlcanciaUpdateDTO("Test", null);
        when(service.update(eq(99), any(AlcanciaUpdateDTO.class)))
                .thenThrow(new IllegalArgumentException("Alcancía no existe"));

        // Act & Assert
        mvc.perform(put("/api/alcancias/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void cuandoEliminar_conIdExistente_entoncesRetorna204NoContent() throws Exception {
        // Arrange
        // service.delete es void, usamos doNothing()
        doNothing().when(service).delete(1);

        // Act & Assert
        mvc.perform(delete("/api/alcancias/1"))
                .andExpect(status().isNoContent()); // 204
    }

    @Test
    void cuandoEliminar_conIdNoExistente_entoncesRetorna404NotFound() throws Exception {
        // Arrange
        doThrow(new IllegalArgumentException("Alcancía no existe")).when(service).delete(99);

        // Act & Assert
        mvc.perform(delete("/api/alcancias/99"))
                .andExpect(status().isNotFound());
    }

    // --- Pruebas de Endpoints Personalizados ---

    @Test
    void cuandoDonadores_conIdExistente_entoncesRetorna200OK_yListaFiltrada() throws Exception {
        // Arrange
        // 1. El service.findById(1) debe funcionar (para el try-catch)
        when(service.findById(1)).thenReturn(alcancia1);
        // 2. El donacionesRepo.findAll() devuelve AMBAS donaciones
        when(donacionesRepo.findAll()).thenReturn(List.of(donacion1, donacionDeOtraAlcancia));

        // Act & Assert
        mvc.perform(get("/api/alcancias/1/donaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1))) // Debe filtrar 'donacionDeOtraAlcancia'
                .andExpect(jsonPath("$[0].usuarioId", is(10)))
                .andExpect(jsonPath("$[0].nombre", is("Donador Uno")))
                .andExpect(jsonPath("$[0].cantidad", is(50.00)));
    }

    @Test
    void cuandoDonadores_conIdNoExistente_entoncesRetorna404NotFound() throws Exception {
        // Arrange
        when(service.findById(99)).thenThrow(new IllegalArgumentException("No existe"));

        // Act & Assert
        mvc.perform(get("/api/alcancias/99/donaciones"))
                .andExpect(status().isNotFound());
        
        // Verificamos que no se intentó buscar en donacionesRepo
        verify(donacionesRepo, never()).findAll();
    }

    @Test
    void cuandoResumen_conIdExistente_entoncesRetorna200OK_yObjetoResumen() throws Exception {
        // Arrange
        when(service.findById(1)).thenReturn(alcancia1);
        when(donacionesRepo.findAll()).thenReturn(List.of(donacion1, donacionDeOtraAlcancia));

        // Act & Assert
        mvc.perform(get("/api/alcancias/1/resumen"))
                .andExpect(status().isOk())
                // Verifica campos de la alcancía
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.descr", is("Ahorro para Viaje")))
                // Verifica la lista anidada de donadores (filtrada)
                .andExpect(jsonPath("$.donadores", hasSize(1)))
                .andExpect(jsonPath("$.donadores[0].usuarioId", is(10)));
    }
}