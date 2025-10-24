package sv.edu.udb.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import sv.edu.udb.demo.security.DbUserDetailsService;
import sv.edu.udb.demo.security.JwtUtil;

import java.util.List;
import java.util.Map;

// Importaciones estáticas para MockMvc, Mockito y Hamcrest
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper; // Para convertir el Map a JSON

    @MockBean
    private AuthenticationManager authManager;

    @MockBean
    private DbUserDetailsService userDetailsService;

    @MockBean
    private JwtUtil jwtUtil;

    private Map<String, String> loginRequestBody;

    @BeforeEach
    void setUp() {
        // Cuerpo de la petición que enviaremos en las pruebas
        loginRequestBody = Map.of(
                "username", "testuser",
                "password", "testpass"
        );
    }

    @Test
    void cuandoLoginEsExitoso_entoncesRetorna200OK_yTokenJWT() throws Exception {
        // 1. Arrange (Preparar)
        
        String username = "testuser";
        String role = "ROLE_USER";
        String fakeToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsInJvbGUiOiJST0xFX1VTRVIifQ.fake-signature";

        // a) Simulamos que authManager.authenticate() NO lanza excepción
        //    (No es necesario mockear 'authenticate' si no lanza excepción, 
        //    el mock simplemente no hará nada, que es lo que queremos)

        // b) Simulamos el UserDetailsService
        //    Usamos la clase 'User' de Spring Security para crear un UserDetails fácilmente
        UserDetails mockUserDetails = new User(
                username,
                "password-hasheada-no-importa",
                List.of(new SimpleGrantedAuthority(role)) // La autoridad que el controller buscará
        );
        when(userDetailsService.loadUserByUsername(username)).thenReturn(mockUserDetails);

        // c) Simulamos el JwtUtil
        when(jwtUtil.generateToken(username, role)).thenReturn(fakeToken);

        // 2. Act & 3. Assert (Actuar y Verificar)
        mvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequestBody))) // Envía el Map como JSON
                .andExpect(status().isOk()) // 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token", is(fakeToken))); // Verifica que el token está en la respuesta

        // Verificamos que los mocks fueron llamados
        verify(authManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService, times(1)).loadUserByUsername(username);
        verify(jwtUtil, times(1)).generateToken(username, role);
    }

    @Test
    void cuandoLoginFalla_porBadCredentials_entoncesRetorna401Unauthorized() throws Exception {
        // 1. Arrange (Preparar)
        
        // Simulamos que el authManager SÍ lanza la excepción
        String mensajeError = "Credenciales Inválidas";
        doThrow(new BadCredentialsException(mensajeError))
                .when(authManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        // 2. Act & 3. Assert (Actuar y Verificar)
        mvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequestBody)))
                .andExpect(status().isUnauthorized()) // 401
                .andExpect(content().string(mensajeError)); // Verifica el mensaje de error en el body

        // Verificamos que la ejecución se detuvo y NUNCA se intentó
        // cargar el user details o generar un token
        verify(userDetailsService, never()).loadUserByUsername(anyString());
        verify(jwtUtil, never()).generateToken(anyString(), anyString());
    }
}