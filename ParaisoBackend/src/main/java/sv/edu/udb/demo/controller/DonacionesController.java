package sv.edu.udb.demo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity; // Añadido si no estaba
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sv.edu.udb.demo.dto.DonacionesCreateDTO;
import sv.edu.udb.demo.dto.DonacionDTO; // <-- IMPORTAR EL NUEVO DTO
import sv.edu.udb.demo.model.Donaciones;
import sv.edu.udb.demo.model.Usuario;
import sv.edu.udb.demo.repository.DonacionesRepository;
import sv.edu.udb.demo.repository.UsuarioRepository;
import sv.edu.udb.demo.service.DonacionesService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors; // <-- IMPORTAR COLLECTORS

@RestController
@RequestMapping("/api/donaciones")
@RequiredArgsConstructor
public class DonacionesController {

    private final DonacionesService service;
    private final UsuarioRepository usuarioRepo;
    private final DonacionesRepository donacionesRepo;

    // Con JWT: el principal trae el username (correo)
    @PostMapping
    public ResponseEntity<Donaciones> crear(@AuthenticationPrincipal(expression = "username") String username, // Cambiado a ResponseEntity
                                            @Valid @RequestBody DonacionesCreateDTO dto) {
        try {
            Usuario u = usuarioRepo.findByCorreo(username)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario app no encontrado"));
            Donaciones donacionCreada = service.crearDonacion(u.getId(), dto);
            // Devolvemos 201 Created si es exitoso
            return ResponseEntity.status(HttpStatus.CREATED).body(donacionCreada);
        } catch (IllegalArgumentException e) {
            // Devolvemos 400 Bad Request si algo falla (usuario/alcancia no existe, monto<=0)
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            // Error genérico del servidor
            return ResponseEntity.internalServerError().build();
        }
    }


    // Reporte con filtros (ADMIN)
    @GetMapping
    public List<DonacionDTO> listar( // <-- CAMBIADO: Devuelve DTO
                                     @RequestParam(required = false) Integer usuarioId,
                                     @RequestParam(required = false) Integer alcanciaId,
                                     @RequestParam(required = false) BigDecimal montoMin,
                                     @RequestParam(required = false) BigDecimal montoMax,
                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta
    ) {
        var all = donacionesRepo.findAll();
        LocalDateTime from = (desde == null) ? null : desde.atStartOfDay();
        LocalDateTime to   = (hasta == null) ? null : hasta.plusDays(1).atStartOfDay();

        return all.stream()
                .filter(d -> usuarioId == null || (d.getUsuario()!=null && usuarioId.equals(d.getUsuario().getId())))
                .filter(d -> alcanciaId == null || (d.getAlcancia()!=null && alcanciaId.equals(d.getAlcancia().getId())))
                .filter(d -> montoMin == null || (d.getCantidadDonada()!=null && d.getCantidadDonada().compareTo(montoMin) >= 0))
                .filter(d -> montoMax == null || (d.getCantidadDonada()!=null && d.getCantidadDonada().compareTo(montoMax) <= 0))
                .filter(d -> from == null || (d.getFecha()!=null && !d.getFecha().isBefore(from)))
                .filter(d -> to == null || (d.getFecha()!=null && d.getFecha().isBefore(to)))
                .map(DonacionDTO::fromEntity) // <-- CAMBIADO: Mapear a DTO
                .collect(Collectors.toList()); // <-- CAMBIADO: Usar collect
    }
}
