package sv.edu.udb.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sv.edu.udb.demo.dto.ProyectosCreateDTO;
import sv.edu.udb.demo.dto.ProyectosUpdateDTO;
import sv.edu.udb.demo.model.Proyectos;
import sv.edu.udb.demo.service.ProyectosService;

import java.util.List;

// Importaciones estáticas para MockMvc, Mockito y Hamcrest
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(ProyectosController.class)
public class ProyectosControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProyectosService service;

    // --- Datos de prueba para el filtrado ---
    private Proyectos p1, p2, p3;

    @BeforeEach
    void setUp() {
        p1 = Proyectos.builder().id(1).tit("Limpieza de Playa").descr("Recoger basura").build();
        p2 = Proyectos.builder().id(2).tit("Reforestación").descr("Plantar árboles").build();
        p3 = Proyectos.builder().id(3).tit("Comedor Canino").descr("Alimentar perros").build();
        
        // Mockeamos la respuesta base del servicio (la lista completa)
        when(service.findAll()).thenReturn(List.of(p1, p2, p3));
    }

    // --- Pruebas GET /api/proyectos (Filtrado con 'q') ---

    @Test
    void cuandoListar_sinFiltroQ_entoncesRetornaListaCompleta() throws Exception {
        mvc.perform(get("/api/proyectos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3))); // p1, p2, p3
    }

    @Test
    void cuandoListar_conFiltroQEnTitulo_entoncesRetornaListaFiltrada() throws Exception {
        // Busca "playa" (case-insensitive)
        mvc.perform(get("/api/proyectos?q=playa"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1))) // p1
                .andExpect(jsonPath("$[0].id", is(p1.getId())));
    }

    @Test
    void cuandoListar_conFiltroQEnDescripcion_entoncesRetornaListaFiltrada() throws Exception {
        // Busca "alimentar" (case-insensitive)
        mvc.perform(get("/api/proyectos?q=alimentar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1))) // p3
                .andExpect(jsonPath("$[0].id", is(p3.getId())));
    }
    
    @Test
    void cuandoListar_conFiltroQEnAmbosCampos_entoncesRetornaListaFiltrada() throws Exception {
        // "re" coincide con "Recoger" (p1) y "Reforestación" (p2)
        mvc.perform(get("/api/proyectos?q=re"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2))) // p1, p2
                .andExpect(jsonPath("$[0].id", is(p1.getId())))
                .andExpect(jsonPath("$[1].id", is(p2.getId())));
    }

    @Test
    void cuandoListar_conFiltroQSinResultados_entoncesRetornaListaVacia() throws Exception {
        mvc.perform(get("/api/proyectos?q=xyz999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0))); // 200 OK con []
    }

    // --- Pruebas GET /api/proyectos/{id} ---

    @Test
    void cuandoPorId_conIdExistente_entoncesRetorna200OK() throws Exception {
        when(service.findById(1)).thenReturn(p1);

        mvc.perform(get("/api/proyectos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(p1.getId())))
                .andExpect(jsonPath("$.tit", is(p1.getTit())));
    }

    @Test
    void cuandoPorId_conIdNoExistente_entoncesRetorna404NotFound() throws Exception {
        when(service.findById(99)).thenThrow(new IllegalArgumentException("Proyecto no existe"));

        mvc.perform(get("/api/proyectos/99"))
                .andExpect(status().isNotFound());
    }

    // --- Pruebas POST /api/proyectos ---

    @Test
    void cuandoCrear_conDTOValidoYImg_entoncesRetorna201Created() throws Exception {
        ProyectosCreateDTO dto = new ProyectosCreateDTO("Proyecto Nuevo", "Desc", "url.jpg");
        Proyectos proyectoCreado = Proyectos.builder().id(4).tit(dto.tit()).img(dto.img()).build();

        when(service.create(any(ProyectosCreateDTO.class))).thenReturn(proyectoCreado);

        mvc.perform(post("/api/proyectos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(4)))
                .andExpect(jsonPath("$.tit", is("Proyecto Nuevo")));
    }
    
    @Test
    void cuandoCrear_conImgNull_entoncesRetorna400BadRequest() throws Exception {
        ProyectosCreateDTO dto = new ProyectosCreateDTO("Proyecto Nuevo", "Desc", null);

        mvc.perform(post("/api/proyectos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("La imagen (img) es obligatoria")));
        
        verify(service, never()).create(any(ProyectosCreateDTO.class));
    }
    
    @Test
    void cuandoCrear_conImgBlank_entoncesRetorna400BadRequest() throws Exception {
        ProyectosCreateDTO dto = new ProyectosCreateDTO("Proyecto Nuevo", "Desc", "   ");

        mvc.perform(post("/api/proyectos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("La imagen (img) es obligatoria")));
        
        verify(service, never()).create(any(ProyectosCreateDTO.class));
    }

    // --- Pruebas PUT /api/proyectos/{id} ---

    @Test
    void cuandoActualizar_conDTOValido_entoncesRetorna200OK() throws Exception {
        ProyectosUpdateDTO dto = new ProyectosUpdateDTO("Título Actualizado", null, "nueva.jpg");
        p1.setTit("Título Actualizado");
        p1.setImg("nueva.jpg");
        
        when(service.update(eq(1), any(ProyectosUpdateDTO.class))).thenReturn(p1);

        mvc.perform(put("/api/proyectos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tit", is("Título Actualizado")))
                .andExpect(jsonPath("$.img", is("nueva.jpg")));
    }
    
    @Test
    void cuandoActualizar_conIdNoExistente_entoncesRetorna404NotFound() throws Exception {
        ProyectosUpdateDTO dto = new ProyectosUpdateDTO("Test", null, null);
        when(service.update(eq(99), any(ProyectosUpdateDTO.class)))
                .thenThrow(new IllegalArgumentException("Proyecto no existe"));

        mvc.perform(put("/api/proyectos/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void cuandoActualizar_conImgBlank_entoncesRetorna400BadRequest() throws Exception {
        ProyectosUpdateDTO dto = new ProyectosUpdateDTO(null, null, "   ");

        mvc.perform(put("/api/proyectos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("La imagen (img) no puede ser vacía")));

        verify(service, never()).update(anyInt(), any(ProyectosUpdateDTO.class));
    }

    // --- Pruebas DELETE /api/proyectos/{id} ---

    @Test
    void cuandoEliminar_conIdExistente_entoncesRetorna204NoContent() throws Exception {
        doNothing().when(service).delete(1);

        mvc.perform(delete("/api/proyectos/1"))
                .andExpect(status().isNoContent()); // 204
    }

    @Test
    void cuandoEliminar_conIdNoExistente_entoncesRetorna404NotFound() throws Exception {
        doThrow(new IllegalArgumentException("Proyecto no existe")).when(service).delete(99);

        mvc.perform(delete("/api/proyectos/99"))
                .andExpect(status().isNotFound());
    }
}