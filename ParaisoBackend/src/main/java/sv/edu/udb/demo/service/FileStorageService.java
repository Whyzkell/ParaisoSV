package sv.edu.udb.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    private static final Set<String> ALLOWED = Set.of("image/jpeg", "image/png", "image/webp");

    public String store(MultipartFile file) throws IOException {
        if (file.isEmpty()) throw new IllegalArgumentException("Archivo vacío");
        if (!ALLOWED.contains(file.getContentType()))
            throw new IllegalArgumentException("Tipo no permitido (jpg, png, webp)");

        String original = StringUtils.cleanPath(file.getOriginalFilename());
        String ext = "";
        int dot = original.lastIndexOf('.');
        if (dot >= 0) ext = original.substring(dot).toLowerCase();

        String filename = UUID.randomUUID() + ext;

        Path dir = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(dir);

        Path target = dir.resolve(filename);
        file.transferTo(target);

        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/files/")
                .path(filename)
                .toUriString();
    }
}
