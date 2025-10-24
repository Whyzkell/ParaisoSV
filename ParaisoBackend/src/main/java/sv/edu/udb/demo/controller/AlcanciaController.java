package sv.edu.udb.demo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.edu.udb.demo.dto.AlcanciaCreateDTO;
import sv.edu.udb.demo.dto.AlcanciaUpdateDTO;
import sv.edu.udb.demo.model.Alcancia;
import sv.edu.udb.demo.model.Donaciones;
import sv.edu.udb.demo.repository.DonacionesRepository;
import sv.edu.udb.demo.service.AlcanciaService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/alcancias")
@RequiredArgsConstructor
public class AlcanciaController {

    private final AlcanciaService service;
    private final DonacionesRepository donacionesRepo;

    @GetMapping
    public List<Alcancia> listar() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Alcancia> porId(@PathVariable Integer id) {
        try { return ResponseEntity.ok(service.findById(id)); }
        catch (IllegalArgumentException ex) { return ResponseEntity.notFound().build(); }
    }

    // Donadores de una alcancía
    @GetMapping("/{id}/donaciones")
    public ResponseEntity<List<DonadorView>> donadores(@PathVariable Integer id) {
        try {
            service.findById(id); // asegura existencia
            List<DonadorView> out = donacionesRepo.findAll().stream()
                    .filter(d -> d.getAlcancia()!=null && id.equals(d.getAlcancia().getId()))
                    .map(DonadorView::from)
                    .toList();
            return ResponseEntity.ok(out);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // Resumen: alcancía + lista de donadores
    @GetMapping("/{id}/resumen")
    public ResponseEntity<AlcanciaResumen> resumen(@PathVariable Integer id) {
        try {
            Alcancia alc = service.findById(id);
            List<DonadorView> donadores = donacionesRepo.findAll().stream()
                    .filter(d -> d.getAlcancia()!=null && id.equals(d.getAlcancia().getId()))
                    .map(DonadorView::from)
                    .toList();
            return ResponseEntity.ok(new AlcanciaResumen(
                    alc.getId(), alc.getDescr(), alc.getPrecioMeta(), alc.getPrecioActual(), donadores
            ));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Alcancia> crear(@Valid @RequestBody AlcanciaCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Alcancia> actualizar(@PathVariable Integer id,
                                               @Valid @RequestBody AlcanciaUpdateDTO dto) {
        try { return ResponseEntity.ok(service.update(id, dto)); }
        catch (IllegalArgumentException ex) { return ResponseEntity.notFound().build(); }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        try { service.delete(id); return ResponseEntity.noContent().build(); }
        catch (IllegalArgumentException ex) { return ResponseEntity.notFound().build(); }
    }

    // DTOs de salida internos
    public record DonadorView(Integer usuarioId, String nombre, BigDecimal cantidad, LocalDateTime fecha) {
        static DonadorView from(Donaciones d) {
            Integer uid = (d.getUsuario()!=null) ? d.getUsuario().getId() : null;
            String nom = (d.getUsuario()!=null) ? d.getUsuario().getNombre() : null;
            return new DonadorView(uid, nom, d.getCantidadDonada(), d.getFecha());
        }
    }
    public record AlcanciaResumen(Integer id, String descr, BigDecimal precioMeta, BigDecimal precioActual,
                                  List<DonadorView> donadores) {}
}
