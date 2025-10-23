package sv.edu.udb.demo.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sv.edu.udb.demo.model.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")   // usa H2 o la config de pruebas que tengas
@Transactional
class RepositoriosTests {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AlcanciaRepository alcanciaRepository;

    @Autowired
    private DonacionesRepository donacionesRepository;

    @Autowired
    private PerroEnAdopcionRepository perroRepository;

    @Autowired
    private ProyectosRepository proyectosRepository;

    // ================== Usuario ==================
    @Test
    void guardarYBuscarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNombre("Carlos");
        usuario.setCorreo("carlos@gmail.com");
        usuario.setPassword("1234");
        usuario.setRol("ADMIN");

        usuarioRepository.save(usuario);

        Optional<Usuario> encontrado = usuarioRepository.findByCorreo("carlos@gmail.com");

        assertTrue(encontrado.isPresent());
        assertEquals("Carlos", encontrado.get().getNombre());
        // si tu columna tiene default de fecha/creación, esto será no-nulo:
        // si no, puedes comentar esta línea
        assertNotNull(encontrado.get().getCreadoEn());
    }

    // ================== Alcancia ==================
    @Test
    void guardarYBuscarAlcancia() {
        Alcancia a = new Alcancia();
        a.setDescr("Alcancia test");
        a.setPrecioMeta(new BigDecimal("1000.00"));
        a.setPrecioActual(BigDecimal.ZERO);

        Alcancia guardada = alcanciaRepository.save(a);

        Optional<Alcancia> encontrado = alcanciaRepository.findById(guardada.getId());
        assertTrue(encontrado.isPresent());
        assertEquals("Alcancia test", encontrado.get().getDescr());
        assertEquals(0, new BigDecimal("1000.00").compareTo(encontrado.get().getPrecioMeta()));
        assertEquals(0, BigDecimal.ZERO.compareTo(encontrado.get().getPrecioActual()));
        assertNotNull(encontrado.get().getCreadoEn());
    }

    // ================== Donaciones ==================
    @Test
    void guardarYBuscarDonacion() {
        Usuario u = new Usuario();
        u.setNombre("Pedro");
        u.setCorreo("pedro@gmail.com");
        u.setPassword("1234");
        u.setRol("USER");
        usuarioRepository.save(u);

        Alcancia a = new Alcancia();
        a.setDescr("Alcancia 1");
        a.setPrecioMeta(new BigDecimal("500.00"));
        a.setPrecioActual(BigDecimal.ZERO);
        alcanciaRepository.save(a);

        Donaciones d = new Donaciones();
        d.setUsuario(u);
        d.setAlcancia(a);
        d.setCantidadDonada(new BigDecimal("50.00"));
        donacionesRepository.save(d);

        List<Donaciones> donaciones = donacionesRepository.findAll();
        assertFalse(donaciones.isEmpty());
        assertEquals(0, new BigDecimal("50.00").compareTo(donaciones.get(0).getCantidadDonada()));
        assertNotNull(donaciones.get(0).getFecha());
    }

    // ================== PerroEnAdopcion ==================
    @Test
    void guardarYBuscarPerro() {
        PerroEnAdopcion p = new PerroEnAdopcion();
        p.setNombre("Fido");
        p.setRaza("Labrador");
        p.setEdad(3);
        p.setDescr("Perro amigable");

        PerroEnAdopcion guardado = perroRepository.save(p);

        Optional<PerroEnAdopcion> encontrado = perroRepository.findById(guardado.getId());
        assertTrue(encontrado.isPresent());
        assertEquals("Fido", encontrado.get().getNombre());
        assertNotNull(encontrado.get().getCreadoEn());
    }

    // ================== Proyectos ==================
    @Test
    void guardarYBuscarProyecto() {
        Proyectos p = new Proyectos();
        p.setTit("Proyecto ABC");
        p.setDescr("Descripción ABC");
        p.setImg("http://img.com/proyecto.png");

        Proyectos guardado = proyectosRepository.save(p);

        Optional<Proyectos> encontrado = proyectosRepository.findById(guardado.getId());
        assertTrue(encontrado.isPresent());
        assertEquals("Proyecto ABC", encontrado.get().getTit());
        assertNotNull(encontrado.get().getCreadoEn());
    }
}
