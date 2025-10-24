package sv.edu.udb.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sv.edu.udb.demo.service.FileStorageService;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileStorageService storage;

    // Admin sube imágenes (form-data con key 'file')
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> upload(@RequestPart("file") MultipartFile file) throws IOException {
        String url = storage.store(file);
        return ResponseEntity.ok(Map.of("url", url));
    }
}
