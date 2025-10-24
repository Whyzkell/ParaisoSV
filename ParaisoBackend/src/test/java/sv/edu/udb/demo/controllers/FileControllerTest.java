package sv.edu.udb.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;
import sv.edu.udb.demo.service.FileStorageService;

import java.io.IOException;

// Importaciones estáticas para MockMvc, Mockito y Hamcrest
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

@WebMvcTest(FileController.class)
public class FileControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private FileStorageService storage;

    @Test
    void cuandoUpload_conArchivoValido_entoncesRetorna200OK_yUrl() throws Exception {
        // 1. Arrange
        String urlGenerada = "http://localhost/files/uuid-12345.jpg";
        
        // Creamos un archivo 'mock' para la subida
        MockMultipartFile file = new MockMultipartFile(
                "file",               // El nombre del @RequestPart ("file")
                "test-imagen.jpg",    // Nombre original del archivo
                "image/jpeg",         // Content Type
                "contenido-de-imagen-fake".getBytes() // Contenido del archivo
        );

        // Simulamos que el servicio de storage procesa el archivo y devuelve la URL
        when(storage.store(any(MultipartFile.class))).thenReturn(urlGenerada);

        // 2. Act & 3. Assert
        mvc.perform(multipart("/api/files") // Usamos .multipart()
                .file(file)) // Adjuntamos el archivo mock
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.url", is(urlGenerada))); // Verificamos la URL en el JSON

        // Verificamos que el servicio SÍ fue llamado
        verify(storage, times(1)).store(any(MultipartFile.class));
    }

    @Test
    void cuandoUpload_conArchivoTipoInvalido_entoncesRetorna400BadRequest() throws Exception {
        // 1. Arrange
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "documento.pdf",
                "application/pdf",
                "contenido-pdf".getBytes()
        );

        // Simulamos la excepción que lanza el servicio (que probamos antes)
        String errorMsg = "Tipo no permitido (jpg, png, webp)";
        when(storage.store(any(MultipartFile.class))).thenThrow(new IllegalArgumentException(errorMsg));

        // 2. Act & 3. Assert
        mvc.perform(multipart("/api/files").file(file))
                // IllegalArgumentException es manejada por Spring como 400 Bad Request
                .andExpect(status().isBadRequest()); 
    }

    @Test
    void cuandoUpload_fallaPorIOException_entoncesRetorna500InternalServerError() throws Exception {
        // 1. Arrange
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.jpg", "image/jpeg", "bytes".getBytes()
        );

        // Simulamos un error de I/O (ej. disco lleno)
        when(storage.store(any(MultipartFile.class))).thenThrow(new IOException("Error al escribir en disco"));

        // 2. Act & 3. Assert
        mvc.perform(multipart("/api/files").file(file))
                // IOException (que no es de cliente) es manejada como 500
                .andExpect(status().isInternalServerError());
    }

    @Test
    void cuandoUpload_sinParteDeArchivo_entoncesRetorna400BadRequest() throws Exception {
        // 1. Arrange
        // No adjuntamos ningún archivo .file()

        // 2. Act & 3. Assert
        mvc.perform(multipart("/api/files"))
                // Spring detecta que el @RequestPart("file") requerido falta
                .andExpect(status().isBadRequest()); 
        
        // Verificamos que el servicio NUNCA fue llamado
        verify(storage, never()).store(any(MultipartFile.class));
    }
}