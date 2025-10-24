package sv.edu.udb.demo.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class FileStorageServiceTest {

    // Mock del archivo que se "sube"
    @Mock
    private MultipartFile file;

    // Directorio temporal que JUnit crea y destruye
    @TempDir
    private Path tempDir;

    // La instancia del servicio que estamos probando
    private FileStorageService fileStorageService;

    @BeforeEach
    void setUp() {
        // 1. Instanciamos el servicio manualmente
        fileStorageService = new FileStorageService();

        // 2. Inyectamos nuestro 'tempDir' en el campo '@Value' del servicio
        // Esto simula que app.upload.dir="ruta/a/tempDir"
        ReflectionTestUtils.setField(fileStorageService, "uploadDir", tempDir.toString());

        // 3. Simulamos un contexto web
        // ServletUriComponentsBuilder.fromCurrentContextPath() necesita esto.
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContextPath(""); // Simula estar en la raíz (http://localhost)
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);
    }

    @AfterEach
    void tearDown() {
        // Limpiamos el contexto web simulado después de cada prueba
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void whenStoreValidFile_thenSavesFileAndReturnsUrl() throws IOException {
        // 1. Arrange
        String originalFilename = "mi-imagen.jpg";
        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("image/jpeg"); // Tipo permitido
        when(file.getOriginalFilename()).thenReturn(originalFilename);

        // Creamos un capturador para el 'Path' que se pasa a 'transferTo'
        ArgumentCaptor<Path> pathCaptor = ArgumentCaptor.forClass(Path.class);

        // Simulamos la llamada a 'transferTo' (que es void)
        doNothing().when(file).transferTo(pathCaptor.capture());

        // 2. Act
        String resultUrl = fileStorageService.store(file);

        // 3. Assert
        // A) Verificar que el archivo se "transfirió"
        verify(file, times(1)).transferTo(any(Path.class));

        // B) Analizar la ruta capturada
        Path capturedPath = pathCaptor.getValue();
        String generatedFilename = capturedPath.getFileName().toString();

        // Verificamos que el archivo se iba a guardar en nuestro tempDir
        assertThat(capturedPath.getParent()).isEqualTo(tempDir.toAbsolutePath().normalize());
        // Verificamos que el nombre es un UUID + .jpg
        assertThat(generatedFilename).endsWith(".jpg");
        assertThat(generatedFilename.length()).isEqualTo(36 + 4); // 36 (UUID) + 4 (.jpg)
        assertThat(generatedFilename).matches("^[0-9a-f-]{36}\\.jpg$");

        // C) Verificar la URL de retorno
        String expectedUrl = "http://localhost/files/" + generatedFilename;
        assertThat(resultUrl).isEqualTo(expectedUrl);
    }

    @Test
    void whenStoreEmptyFile_thenThrowsException() throws IOException { // <-- ARREGLADO
        // 1. Arrange
        when(file.isEmpty()).thenReturn(true);

        // 2. Act & 3. Assert
        assertThatThrownBy(() -> fileStorageService.store(file))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Archivo vacío");

        // Verificamos que nunca se intentó guardar nada
        verify(file, never()).transferTo(any(Path.class));
    }

    @Test
    void whenStoreInvalidContentType_thenThrowsException() throws IOException { // <-- ARREGLADO
        // 1. Arrange
        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("application/pdf"); // Tipo no permitido

        // 2. Act & 3. Assert
        assertThatThrownBy(() -> fileStorageService.store(file))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Tipo no permitido (jpg, png, webp)");

        verify(file, never()).transferTo(any(Path.class));
    }

    @Test
    void whenStoreFileWithNoExtension_thenSavesWithUUIDOnly() throws IOException {
        // 1. Arrange
        String originalFilename = "archivo-sin-extension";
        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("image/png"); // Tipo permitido
        when(file.getOriginalFilename()).thenReturn(originalFilename);

        ArgumentCaptor<Path> pathCaptor = ArgumentCaptor.forClass(Path.class);
        doNothing().when(file).transferTo(pathCaptor.capture());

        // 2. Act
        String resultUrl = fileStorageService.store(file);

        // 3. Assert
        String generatedFilename = pathCaptor.getValue().getFileName().toString();

        // Verificamos que el nombre es solo el UUID (longitud 36)
        assertThat(generatedFilename.length()).isEqualTo(36);
        assertThat(generatedFilename).doesNotContain(".");
        assertThat(resultUrl).isEqualTo("http://localhost/files/" + generatedFilename);
    }
}