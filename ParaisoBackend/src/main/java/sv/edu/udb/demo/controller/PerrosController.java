package sv.edu.udb.demo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.edu.udb.demo.dto.PerroCreateDTO;
import sv.edu.udb.demo.dto.PerroUpdateDTO;
import sv.edu.udb.demo.model.PerroEnAdopcion;
import sv.edu.udb.demo.service.PerroEnAdopcionService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/perros")
@RequiredArgsConstructor
public class PerrosController {

    private final PerroEnAdopcionService service;

    // públicos
    @GetMapping
    public List<PerroEnAdopcion> listar(@RequestParam(required = false) String nombre,
                                        @RequestParam(required = false) String raza) {
        return service.findAll().stream()
                .filter(p -> nombre == null || (p.getNombre()!=null && p.getNombre().toLowerCase().contains(nombre.toLowerCase())))
                .filter(p -> raza   == null || (p.getRaza()!=null && p.getRaza().toLowerCase().contains(raza.toLowerCase())))
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PerroEnAdopcion> porId(@PathVariable Integer id) {
        try { return ResponseEntity.ok(service.findById(id)); }
        catch (IllegalArgumentException ex) { return ResponseEntity.notFound().build(); }
    }

    // admin: img obligatoria
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody PerroCreateDTO dto) {
        if (dto.img()==null || dto.img().isBlank())
            return ResponseEntity.badRequest().body(Map.of("error", "La imagen (img) es obligatoria"));
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @Valid @RequestBody PerroUpdateDTO dto) {
        if (dto.img()!=null && dto.img().isBlank())
            return ResponseEntity.badRequest().body(Map.of("error", "La imagen (img) no puede ser vacía"));
        try { return ResponseEntity.ok(service.update(id, dto)); }
        catch (IllegalArgumentException ex) { return ResponseEntity.notFound().build(); }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        try { service.delete(id); return ResponseEntity.noContent().build(); }
        catch (IllegalArgumentException ex) { return ResponseEntity.notFound().build(); }
    }
}
