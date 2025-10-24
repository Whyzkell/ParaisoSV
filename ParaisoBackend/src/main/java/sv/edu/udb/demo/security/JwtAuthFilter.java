package sv.edu.udb.demo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final DbUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws IOException, jakarta.servlet.ServletException {

        System.out.println("➡️ Filtro JWT ejecutándose para: " + request.getRequestURI());

        final String authHeader = request.getHeader("Authorization");
        String username = null;
        String token = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            System.out.println("🔑 Token recibido: " + token);

            if (jwtUtil.isTokenValid(token)) {
                username = jwtUtil.extractUsername(token);
                String role = jwtUtil.extractRole(token);
                System.out.println("✅ Token válido para usuario " + username + " con rol " + role);
            } else {
                System.out.println("❌ Token inválido o expirado");
            }
        } else {
            System.out.println("⚠️ No se envió token");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            var userDetails = userDetailsService.loadUserByUsername(username);

            // ❌ LÍNEA ORIGINAL (Causaba el error ROLE_ROLE_ADMIN):
            // var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + jwtUtil.extractRole(token).toUpperCase()));

            // ✅ LÍNEA CORREGIDA: Usa las autoridades ya cargadas del userDetails (que ya son ROLE_ADMIN)
            var authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);

            System.out.println("🎯 Autenticado: " + username + " con roles: " + userDetails.getAuthorities());
        }

        filterChain.doFilter(request, response);
    }
}