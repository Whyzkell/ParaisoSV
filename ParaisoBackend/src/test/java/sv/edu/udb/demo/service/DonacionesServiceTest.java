package sv.edu.udb.demo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sv.edu.udb.demo.dto.DonacionesCreateDTO;
import sv.edu.udb.demo.model.Alcancia;
import sv.edu.udb.demo.model.Usuario;
import sv.edu.udb.demo.repository.AlcanciaRepository;
import sv.edu.udb.demo.repository.DonacionesRepository;
import sv.edu.udb.demo.repository.UsuarioRepository;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class DonacionesServiceTest {

    @Autowired DonacionesService service;              // tu service REAL
    @Autowired UsuarioRepository usuarioRepo;          // repos REALES
    @Autowired AlcanciaRepository alcanciaRepo;
    @Autowired DonacionesRepository donacionesRepo;

    private Integer userId;
    private Integer alcId;

    @BeforeEach
    void setUp() {
        // Usuario
        Usuario u = Usuario.builder()
                .nombre("Tester")
                .correo("tester@demo.com")
                .password("hash") // no importa en el service
                .rol("USER")
                .build();
        u = usuarioRepo.save(u);
        userId = u.getId();

        // Alcancía
        Alcancia a = Alcancia.builder()
                .descr("Rescate Canino")
                .precioMeta(new BigDecimal("500.00"))
                .precioActual(new BigDecimal("50.00"))
                .build();
        a = alcanciaRepo.save(a);
        alcId = a.getId();
    }

    @Test
    void donar_ok_actualiza_total_y_guarda() {
        var dto = new DonacionesCreateDTO(alcId, new BigDecimal("25.00"));
        var don = service.crearDonacion(userId, dto);

        assertNotNull(don.getId());
        var alc = alcanciaRepo.findById(alcId).orElseThrow();
        assertEquals(new BigDecimal("75.00"), alc.getPrecioActual()); // 50 + 25
        assertEquals(1, donacionesRepo.count());
    }

    @Test
    void donar_falla_usuario_no_existe() {
        var dto = new DonacionesCreateDTO(alcId, new BigDecimal("10.00"));
        var ex = assertThrows(IllegalArgumentException.class,
                () -> service.crearDonacion(999999, dto));
        assertTrue(ex.getMessage().toLowerCase().contains("usuario"));
        assertEquals(0, donacionesRepo.count());
    }

    @Test
    void donar_falla_alcancia_no_existe() {
        var dto = new DonacionesCreateDTO(999999, new BigDecimal("10.00"));
        var ex = assertThrows(IllegalArgumentException.class,
                () -> service.crearDonacion(userId, dto));
        assertTrue(ex.getMessage().toLowerCase().contains("alcanc"));
        assertEquals(0, donacionesRepo.count());
    }

    @Test
    void donar_falla_monto_no_positivo() {
        var dto = new DonacionesCreateDTO(alcId, new BigDecimal("0.00"));
        var ex = assertThrows(IllegalArgumentException.class,
                () -> service.crearDonacion(userId, dto));
        assertTrue(ex.getMessage().toLowerCase().contains("mayor a 0"));
        assertEquals(0, donacionesRepo.count());
    }
}
