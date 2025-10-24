package sv.edu.udb.demo.dto;

import sv.edu.udb.demo.model.Donaciones;
import java.math.BigDecimal;
import java.time.LocalDateTime;

// Usamos 'record' para un DTO simple
public record DonacionDTO(
        Integer id,
        String usuarioNombre, // Solo el nombre, no todo el objeto Usuario
        String alcanciaDescr, // Solo la descripción, no todo el objeto Alcancia
        BigDecimal cantidadDonada,
        LocalDateTime fecha // La fecha original
) {
    // Método estático para convertir fácilmente de Entidad a DTO
    public static DonacionDTO fromEntity(Donaciones donacion) {
        // Usamos '?' para manejar el caso de que usuario o alcancia sean null
        String nombre = donacion.getUsuario() != null ? donacion.getUsuario().getNombre() : "N/A";
        String descr = donacion.getAlcancia() != null ? donacion.getAlcancia().getDescr() : "N/A";

        return new DonacionDTO(
                donacion.getId(),
                nombre,
                descr,
                donacion.getCantidadDonada(),
                donacion.getFecha()
        );
    }
}
