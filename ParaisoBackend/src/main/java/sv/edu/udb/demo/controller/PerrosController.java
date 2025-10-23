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

@RestController
@RequestMapping("/api/perros")
@RequiredArgsConstructor
public class PerrosController {

    private final PerroEnAdopcionService service;

    // GET (con filtros: nombre, raza, edadMin, edadMax)
    @GetMapping
    public List<PerroEnAdopcion> listar(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String raza,
            @RequestParam(required = false) Integer edadMin,
            @RequestParam(required = false) Integer edadMax
    ) {
        return service.findAll().stream()
                .filter(p -> nombre == null || (p.getNombre() != null && p.getNombre().toLowerCase().contains(nombre.toLowerCase())))
                .filter(p -> raza == null || (p.getRaza() != null && p.getRaza().toLowerCase().contains(raza.toLowerCase())))
                .filter(p -> edadMin == null || (p.getEdad() != null && p.getEdad() >= edadMin))
                .filter(p -> edadMax == null || (p.getEdad() != null && p.getEdad() <= edadMax))
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PerroEnAdopcion> porId(@PathVariable Integer id) {
        try { return ResponseEntity.ok(service.findById(id)); }
        catch (IllegalArgumentException ex) { return ResponseEntity.notFound().build(); }
    }

    @PostMapping
    public ResponseEntity<PerroEnAdopcion> crear(@Valid @RequestBody PerroCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PerroEnAdopcion> actualizar(@PathVariable Integer id,
                                                      @Valid @RequestBody PerroUpdateDTO dto) {
        try { return ResponseEntity.ok(service.update(id, dto)); }
        catch (IllegalArgumentException ex) { return ResponseEntity.notFound().build(); }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        try { service.delete(id); return ResponseEntity.noContent().build(); }
        catch (IllegalArgumentException ex) { return ResponseEntity.notFound().build(); }
    }
}
