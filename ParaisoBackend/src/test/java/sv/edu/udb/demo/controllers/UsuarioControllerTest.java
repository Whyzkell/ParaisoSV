package sv.edu.udb.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sv.edu.udb.demo.dto.PasswordChangeDTO;
import sv.edu.udb.demo.dto.UsuarioCreateDTO;
import sv.edu.udb.demo.dto.UsuarioUpdateDTO;
import sv.edu.udb.demo.model.Usuario;
import sv.edu.udb.demo.service.UsuarioService;

import java.util.List;

// Importaciones estáticas para MockMvc, Mockito y Hamcrest
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioService service;

    // --- Datos de prueba para el filtrado ---
    private Usuario user1, user2, user3;

    @BeforeEach
    void setUp() {
        user1 = Usuario.builder().id(1).nombre("Admin User").correo("admin@test.com").rol("ADMIN").build();
        user2 = Usuario.builder().id(2).nombre("Bob").correo("bob@test.com").rol("USER").build();
        user3 = Usuario.builder().id(3).nombre("Charlie User").correo("charlie@demo.com").rol("USER").build();
        
        // Mockeamos la respuesta base del servicio (la lista completa)
        when(service.findAll()).thenReturn(List.of(user1, user2, user3));
    }

    // --- Pruebas GET /api/usuarios (Filtrado) ---

    @Test
    void cuandoListar_sinFiltros_entoncesRetornaListaCompleta() throws Exception {
        mvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    void cuandoListar_conFiltroNombre_entoncesRetornaListaFiltrada() throws Exception {
        // Busca "user" (case-insensitive)
        mvc.perform(get("/api/usuarios?nombre=user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2))) // user1, user3
                .andExpect(jsonPath("$[0].id", is(user1.getId())))
                .andExpect(jsonPath("$[1].id", is(user3.getId())));
    }

    @Test
    void cuandoListar_conFiltroCorreo_entoncesRetornaListaFiltrada() throws Exception {
        // Busca "test.com" (case-insensitive)
        mvc.perform(get("/api/usuarios?correo=test.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2))) // user1, user2
                .andExpect(jsonPath("$[0].id", is(user1.getId())))
                .andExpect(jsonPath("$[1].id", is(user2.getId())));
    }

    @Test
    void cuandoListar_conFiltroRol_entoncesRetornaListaFiltrada() throws Exception {
        // Busca "user" (case-insensitive)
        mvc.perform(get("/api/usuarios?rol=user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2))) // user2, user3
                .andExpect(jsonPath("$[0].id", is(user2.getId())))
                .andExpect(jsonPath("$[1].id", is(user3.getId())));
    }

    @Test
    void cuandoListar_conFiltrosCombinados_entoncesRetornaListaFiltrada() throws Exception {
        // Busca rol "USER" Y nombre "bob"
        mvc.perform(get("/api/usuarios?rol=USER&nombre=bob"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1))) // user2
                .andExpect(jsonPath("$[0].id", is(user2.getId())));
    }

    // --- Pruebas GET /api/usuarios/{id} ---

    @Test
    void cuandoById_conIdExistente_entoncesRetorna200OK() throws Exception {
        when(service.findById(1)).thenReturn(user1);

        mvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Admin User")));
    }

    @Test
    void cuandoById_conIdNoExistente_entoncesRetorna404NotFound() throws Exception {
        when(service.findById(99)).thenThrow(new IllegalArgumentException("Usuario no existe"));

        mvc.perform(get("/api/usuarios/99"))
                .andExpect(status().isNotFound());
    }

    // --- Pruebas POST /api/usuarios ---

    @Test
    void cuandoCreate_conDTOValido_entoncesRetorna201Created() throws Exception {
        UsuarioCreateDTO dto = new UsuarioCreateDTO("Nuevo", "nuevo@test.com", "pass", "USER");
        Usuario usuarioCreado = Usuario.builder().id(4).nombre("Nuevo").correo("nuevo@test.com").rol("USER").build();

        when(service.create(any(UsuarioCreateDTO.class))).thenReturn(usuarioCreado);

        mvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(4)));
    }
    
    @Test
    void cuandoCreate_conDTOInvalido_entoncesRetorna400BadRequest() throws Exception {
        // DTO inválido (correo no es un email)
        UsuarioCreateDTO dtoInvalido = new UsuarioCreateDTO("Nombre", "no-es-email", "pass", "USER");

        mvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoInvalido)))
                .andExpect(status().isBadRequest());
        
        verify(service, never()).create(any(UsuarioCreateDTO.class));
    }
    
    @Test
    void cuandoCreate_conCorreoDuplicado_entoncesRetornaError() throws Exception {
        // Esta prueba asume que el servicio lanza la excepción y Spring la maneja (ej. como 400)
        UsuarioCreateDTO dto = new UsuarioCreateDTO("Nuevo", "admin@test.com", "pass", "USER");
        
        when(service.create(any(UsuarioCreateDTO.class)))
                .thenThrow(new IllegalArgumentException("Correo ya está en uso"));

        mvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest()); // O 409 Conflict, pero 400 es común
    }

    // --- Pruebas PUT /api/usuarios/{id} ---

    @Test
    void cuandoUpdate_conIdExistente_entoncesRetorna200OK() throws Exception {
        UsuarioUpdateDTO dto = new UsuarioUpdateDTO("Bob Actualizado", null, "MODERATOR");
        user2.setNombre("Bob Actualizado");
        user2.setRol("MODERATOR");

        when(service.update(eq(2), any(UsuarioUpdateDTO.class))).thenReturn(user2);

        mvc.perform(put("/api/usuarios/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Bob Actualizado")))
                .andExpect(jsonPath("$.rol", is("MODERATOR")));
    }

    @Test
    void cuandoUpdate_conIdNoExistente_entoncesRetorna404NotFound() throws Exception {
        UsuarioUpdateDTO dto = new UsuarioUpdateDTO("Test", null, null);
        when(service.update(eq(99), any(UsuarioUpdateDTO.class)))
                .thenThrow(new IllegalArgumentException("Usuario no existe"));

        mvc.perform(put("/api/usuarios/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    // --- Pruebas PUT /api/usuarios/{id}/password ---

    @Test
    void cuandoChangePassword_conIdExistente_entoncesRetorna204NoContent() throws Exception {
        PasswordChangeDTO dto = new PasswordChangeDTO("nuevaPass123");
        doNothing().when(service).changePassword(eq(1), any(PasswordChangeDTO.class));

        mvc.perform(put("/api/usuarios/1/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNoContent()); // 204
    }

    @Test
    void cuandoChangePassword_conDTOInvalido_entoncesRetorna400BadRequest() throws Exception {
        // Simulamos un DTO con password en blanco (asumiendo @NotBlank)
        String dtoJsonInvalido = "{\"password\": \"   \"}";

        mvc.perform(put("/api/usuarios/1/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dtoJsonInvalido))
                .andExpect(status().isBadRequest());
        
        verify(service, never()).changePassword(anyInt(), any(PasswordChangeDTO.class));
    }
    
    @Test
    void cuandoChangePassword_conIdNoExistente_entoncesRetorna404NotFound() throws Exception {
        PasswordChangeDTO dto = new PasswordChangeDTO("nuevaPass123");
        doThrow(new IllegalArgumentException("Usuario no existe"))
                .when(service).changePassword(eq(99), any(PasswordChangeDTO.class));

        mvc.perform(put("/api/usuarios/99/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    // --- Pruebas DELETE /api/usuarios/{id} ---

    @Test
    void cuandoDelete_conIdExistente_entoncesRetorna204NoContent() throws Exception {
        doNothing().when(service).delete(1);
        mvc.perform(delete("/api/usuarios/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void cuandoDelete_conIdNoExistente_entoncesRetorna404NotFound() throws Exception {
        doThrow(new IllegalArgumentException("Usuario no existe")).when(service).delete(99);
        mvc.perform(delete("/api/usuarios/99"))
                .andExpect(status().isNotFound());
    }
}