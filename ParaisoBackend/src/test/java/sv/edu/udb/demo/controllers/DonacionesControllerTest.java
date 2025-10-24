package sv.edu.udb.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
// Importante para simular el @AuthenticationPrincipal
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import org.springframework.test.web.servlet.MockMvc;
import sv.edu.udb.demo.dto.DonacionesCreateDTO;
import sv.edu.udb.demo.model.Alcancia;
import sv.edu.udb.demo.model.Donaciones;
import sv.edu.udb.demo.model.Usuario;
import sv.edu.udb.demo.repository.DonacionesRepository;
import sv.edu.udb.demo.repository.UsuarioRepository;
import sv.edu.udb.demo.service.DonacionesService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

// Importaciones estáticas para MockMvc, Mockito y Hamcrest
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(DonacionesController.class)
public class DonacionesControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DonacionesService service;
    @MockBean
    private UsuarioRepository usuarioRepo;
    @MockBean
    private DonacionesRepository donacionesRepo;

    // --- Datos de prueba para el reporte ---
    private Usuario user1, user2;
    private Alcancia alc1, alc2;
    private Donaciones d1, d2, d3;

    @BeforeEach
    void setUp() {
        // Usuarios
        user1 = Usuario.builder().id(1).nombre("User Uno").correo("user1@test.com").build();
        user2 = Usuario.builder().id(2).nombre("User Dos").correo("user2@test.com").build();
        
        // Alcancias
        alc1 = Alcancia.builder().id(10).descr("Alcancia 10").build();
        alc2 = Alcancia.builder().id(20).descr("Alcancia 20").build();

        // Donaciones (para el findAll del reporte)
        d1 = Donaciones.builder().id(100).usuario(user1).alcancia(alc1)
                .cantidadDonada(new BigDecimal("50.00")).fecha(LocalDateTime.parse("2025-10-20T10:00:00")).build();
        d2 = Donaciones.builder().id(101).usuario(user2).alcancia(alc1)
                .cantidadDonada(new BigDecimal("100.00")).fecha(LocalDateTime.parse("2025-10-21T11:00:00")).build();
        d3 = Donaciones.builder().id(102).usuario(user1).alcancia(alc2)
                .cantidadDonada(new BigDecimal("150.00")).fecha(LocalDateTime.parse("2025-10-22T12:00:00")).build();
        
        // Mock de la lista completa que retorna el repo
        when(donacionesRepo.findAll()).thenReturn(List.of(d1, d2, d3));
    }

    // --- Pruebas del POST /api/donaciones ---

    @Test
    void cuandoCrear_conUsuarioAutenticadoYDTOValido_entoncesRetorna200OK() throws Exception {
        // 1. Arrange
        String usernameAutenticado = "user1@test.com";
        Integer idUsuarioAutenticado = 1;
        DonacionesCreateDTO dto = new DonacionesCreateDTO(10, new BigDecimal("50.00"));

        // Mock: el repo encuentra al usuario por su correo (del token)
        when(usuarioRepo.findByCorreo(usernameAutenticado)).thenReturn(Optional.of(user1));
        
        // Mock: el servicio crea la donación
        Donaciones donacionCreada = Donaciones.builder().id(999).usuario(user1).build();
        when(service.crearDonacion(eq(idUsuarioAutenticado), any(DonacionesCreateDTO.class)))
                .thenReturn(donacionCreada);

        // 2. Act & 3. Assert
        mvc.perform(post("/api/donaciones")
                // Simula el @AuthenticationPrincipal(expression = "username")
                .with(user(usernameAutenticado)) 
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk()) // El método devuelve Donaciones, no ResponseEntity, por eso es 200 OK
                .andExpect(jsonPath("$.id", is(999)));

        // Verificamos que se usó el ID del usuario del token
        verify(service, times(1)).crearDonacion(eq(idUsuarioAutenticado), any(DonacionesCreateDTO.class));
    }

    @Test
    void cuandoCrear_conDTOInvalido_entoncesRetorna400BadRequest() throws Exception {
        // 1. Arrange
        // DTO Inválido: cantidadDonada es null (asumiendo @NotNull)
        String dtoJson = "{\"idAlcancia\": 10}"; // No podemos usar el DTO record porque no compilaría

        // 2. Act & 3. Assert
        mvc.perform(post("/api/donaciones")
                .with(user("user1@test.com")) // Usuario válido
                .contentType(MediaType.APPLICATION_JSON)
                .content(dtoJson))
                .andExpect(status().isBadRequest()); // 400

        // Verificamos que el servicio NUNCA fue llamado
        verify(usuarioRepo, never()).findByCorreo(anyString());
        verify(service, never()).crearDonacion(anyInt(), any(DonacionesCreateDTO.class));
    }

    @Test
    void cuandoCrear_conUsuarioDeTokenNoEncontradoEnDB_entoncesRetorna500() throws Exception {
        // 1. Arrange
        String usernameAutenticado = "ghost@test.com"; // Un usuario que pasó JWT pero no está en la DB
        DonacionesCreateDTO dto = new DonacionesCreateDTO(10, new BigDecimal("50.00"));

        // Mock: el repo NO encuentra al usuario
        when(usuarioRepo.findByCorreo(usernameAutenticado)).thenReturn(Optional.empty());

        // 2. Act & 3. Assert
        mvc.perform(post("/api/donaciones")
                .with(user(usernameAutenticado))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isInternalServerError()); // Causa un unhandled IllegalArgumentException
    }

    // --- Pruebas del GET /api/donaciones (Reporte) ---

    @Test
    void cuandoListar_sinFiltros_entoncesRetornaListaCompleta() throws Exception {
        mvc.perform(get("/api/donaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3))); // d1, d2, d3
    }
    
    @Test
    void cuandoListar_conFiltroUsuarioId_entoncesRetornaListaFiltrada() throws Exception {
        mvc.perform(get("/api/donaciones?usuarioId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2))) // d1, d3
                .andExpect(jsonPath("$[0].id", is(d1.getId())))
                .andExpect(jsonPath("$[1].id", is(d3.getId())));
    }
    
    @Test
    void cuandoListar_conFiltroAlcanciaId_entoncesRetornaListaFiltrada() throws Exception {
        mvc.perform(get("/api/donaciones?alcanciaId=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2))) // d1, d2
                .andExpect(jsonPath("$[0].id", is(d1.getId())))
                .andExpect(jsonPath("$[1].id", is(d2.getId())));
    }

    @Test
    void cuandoListar_conFiltroMontoMin_entoncesRetornaListaFiltrada() throws Exception {
        mvc.perform(get("/api/donaciones?montoMin=100.00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2))) // d2, d3
                .andExpect(jsonPath("$[0].id", is(d2.getId())))
                .andExpect(jsonPath("$[1].id", is(d3.getId())));
    }

    @Test
    void cuandoListar_conFiltroMontoMax_entoncesRetornaListaFiltrada() throws Exception {
        mvc.perform(get("/api/donaciones?montoMax=100.00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2))) // d1, d2
                .andExpect(jsonPath("$[0].id", is(d1.getId())))
                .andExpect(jsonPath("$[1].id", is(d2.getId())));
    }

    @Test
    void cuandoListar_conFiltroDesde_entoncesRetornaListaFiltrada() throws Exception {
        // 'desde' es inclusivo
        mvc.perform(get("/api/donaciones?desde=2025-10-21"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2))) // d2, d3
                .andExpect(jsonPath("$[0].id", is(d2.getId())))
                .andExpect(jsonPath("$[1].id", is(d3.getId())));
    }

    @Test
    void cuandoListar_conFiltroHasta_entoncesRetornaListaFiltrada() throws Exception {
        // 'hasta' es inclusivo (porque la lógica usa plusDays(1) y isBefore)
        mvc.perform(get("/api/donaciones?hasta=2025-10-21"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2))) // d1, d2
                .andExpect(jsonPath("$[0].id", is(d1.getId())))
                .andExpect(jsonPath("$[1].id", is(d2.getId())));
    }
    
    @Test
    void cuandoListar_conFiltrosCombinados_entoncesRetornaListaFiltrada() throws Exception {
        // Busca donaciones del usuario 1 Y para la alcancía 20
        mvc.perform(get("/api/donaciones?usuarioId=1&alcanciaId=20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1))) // solo d3
                .andExpect(jsonPath("$[0].id", is(d3.getId())));
    }

    @Test
    void cuandoListar_conFiltrosSinResultados_entoncesRetornaListaVacia() throws Exception {
        mvc.perform(get("/api/donaciones?usuarioId=999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0))); // 200 OK con lista vacía
    }
}