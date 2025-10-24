package sv.edu.udb.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sv.edu.udb.demo.dto.PerroCreateDTO;
import sv.edu.udb.demo.dto.PerroUpdateDTO;
import sv.edu.udb.demo.model.PerroEnAdopcion;
import sv.edu.udb.demo.service.PerroEnAdopcionService;

import java.util.List;

// Importaciones estáticas para MockMvc, Mockito y Hamcrest
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(PerrosController.class)
public class PerrosControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PerroEnAdopcionService service;

    // --- Datos de prueba para el filtrado ---
    private PerroEnAdopcion p1, p2, p3;

    @BeforeEach
    void setUp() {
        p1 = PerroEnAdopcion.builder().id(1).nombre("Firulais").raza("Pastor Alemán").build();
        p2 = PerroEnAdopcion.builder().id(2).nombre("Max").raza("Boxer").build();
        p3 = PerroEnAdopcion.builder().id(3).nombre("Bobby").raza("Pastor Belga").build();
        
        // Mockeamos la respuesta base del servicio (la lista completa)
        when(service.findAll()).thenReturn(List.of(p1, p2, p3));
    }

    // --- Pruebas GET /api/perros (Filtrado) ---

    @Test
    void cuandoListar_sinFiltros_entoncesRetornaListaCompleta() throws Exception {
        mvc.perform(get("/api/perros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3))); // p1, p2, p3
    }

    @Test
    void cuandoListar_conFiltroNombre_entoncesRetornaListaFiltrada() throws Exception {
        // Busca "max" (case-insensitive)
        mvc.perform(get("/api/perros?nombre=max"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1))) // p2
                .andExpect(jsonPath("$[0].id", is(p2.getId())));
    }

    @Test
    void cuandoListar_conFiltroRaza_entoncesRetornaListaFiltrada() throws Exception {
        // Busca "pastor" (case-insensitive)
        mvc.perform(get("/api/perros?raza=pastor"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2))) // p1, p3
                .andExpect(jsonPath("$[0].id", is(p1.getId())))
                .andExpect(jsonPath("$[1].id", is(p3.getId())));
    }
    
    @Test
    void cuandoListar_conFiltrosCombinados_entoncesRetornaListaFiltrada() throws Exception {
        // Busca nombre "b" Y raza "pastor"
        mvc.perform(get("/api/perros?nombre=b&raza=pastor"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1))) // p3 (Bobby, Pastor Belga)
                .andExpect(jsonPath("$[0].id", is(p3.getId())));
    }

    // --- Pruebas GET /api/perros/{id} ---

    @Test
    void cuandoPorId_conIdExistente_entoncesRetorna200OK_yPerro() throws Exception {
        when(service.findById(1)).thenReturn(p1);

        mvc.perform(get("/api/perros/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(p1.getId())))
                .andExpect(jsonPath("$.nombre", is(p1.getNombre())));
    }

    @Test
    void cuandoPorId_conIdNoExistente_entoncesRetorna404NotFound() throws Exception {
        when(service.findById(99)).thenThrow(new IllegalArgumentException("Perro no existe"));

        mvc.perform(get("/api/perros/99"))
                .andExpect(status().isNotFound());
    }

    // --- Pruebas POST /api/perros ---

    @Test
    void cuandoCrear_conDTOValidoYImg_entoncesRetorna201Created() throws Exception {
        PerroCreateDTO dto = new PerroCreateDTO("Chispita", "Chihuahua", 1, "url.jpg", "desc");
        PerroEnAdopcion perroCreado = PerroEnAdopcion.builder().id(4).nombre(dto.nombre()).img(dto.img()).build();

        when(service.create(any(PerroCreateDTO.class))).thenReturn(perroCreado);

        mvc.perform(post("/api/perros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(4)))
                .andExpect(jsonPath("$.nombre", is("Chispita")));
    }
    
    @Test
    void cuandoCrear_conImgNull_entoncesRetorna400BadRequest() throws Exception {
        // DTO válido según @Valid, pero inválido para la lógica del controlador
        PerroCreateDTO dto = new PerroCreateDTO("Chispita", "Chihuahua", 1, null, "desc");

        mvc.perform(post("/api/perros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("La imagen (img) es obligatoria")));
        
        // Verificamos que el servicio NUNCA fue llamado
        verify(service, never()).create(any(PerroCreateDTO.class));
    }
    
    @Test
    void cuandoCrear_conImgBlank_entoncesRetorna400BadRequest() throws Exception {
        PerroCreateDTO dto = new PerroCreateDTO("Chispita", "Chihuahua", 1, "   ", "desc");

        mvc.perform(post("/api/perros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("La imagen (img) es obligatoria")));
        
        verify(service, never()).create(any(PerroCreateDTO.class));
    }

    // --- Pruebas PUT /api/perros/{id} ---

    @Test
    void cuandoActualizar_conDTOValido_entoncesRetorna200OK() throws Exception {
        PerroUpdateDTO dto = new PerroUpdateDTO("Max Actualizado", null, null, "nueva.jpg", null);
        p2.setNombre("Max Actualizado");
        p2.setImg("nueva.jpg");
        
        when(service.update(eq(2), any(PerroUpdateDTO.class))).thenReturn(p2);

        mvc.perform(put("/api/perros/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Max Actualizado")))
                .andExpect(jsonPath("$.img", is("nueva.jpg")));
    }
    
    @Test
    void cuandoActualizar_conIdNoExistente_entoncesRetorna404NotFound() throws Exception {
        PerroUpdateDTO dto = new PerroUpdateDTO("Test", null, null, null, null);
        when(service.update(eq(99), any(PerroUpdateDTO.class)))
                .thenThrow(new IllegalArgumentException("Perro no existe"));

        mvc.perform(put("/api/perros/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void cuandoActualizar_conImgBlank_entoncesRetorna400BadRequest() throws Exception {
        PerroUpdateDTO dto = new PerroUpdateDTO(null, null, null, "   ", null);

        mvc.perform(put("/api/perros/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("La imagen (img) no puede ser vacía")));

        verify(service, never()).update(anyInt(), any(PerroUpdateDTO.class));
    }

    // --- Pruebas DELETE /api/perros/{id} ---

    @Test
    void cuandoEliminar_conIdExistente_entoncesRetorna204NoContent() throws Exception {
        doNothing().when(service).delete(1);

        mvc.perform(delete("/api/perros/1"))
                .andExpect(status().isNoContent()); // 204
    }

    @Test
    void cuandoEliminar_conIdNoExistente_entoncesRetorna404NotFound() throws Exception {
        doThrow(new IllegalArgumentException("Perro no existe")).when(service).delete(99);

        mvc.perform(delete("/api/perros/99"))
                .andExpect(status().isNotFound());
    }
}