package com.duoc.kiwiapi.Security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//Filtro que se ejecuta una vez en cada peticion y revisa si trae token.
public class JwtFiltro extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFiltro(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        //El token viene asi en el header: Authorization: Bearer xxxxx
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                Claims datos = jwtUtil.leerToken(token);
                String correo = datos.getSubject();
                String rol = datos.get("rol", String.class);

                //Spring espera los roles con ROLE_ adelante, ej: ROLE_ADMINISTRADOR
                var permisos = List.of(new SimpleGrantedAuthority("ROLE_" + rol.toUpperCase()));
                var usuario = new UsernamePasswordAuthenticationToken(correo, null, permisos);

                //Desde aca spring sabe quien es el usuario en esta peticion.
                SecurityContextHolder.getContext().setAuthentication(usuario);
            } catch (JwtException | IllegalArgumentException | NullPointerException e) {
                //Token vencido o malo: no se autentica a nadie.
                //Si la ruta es privada, la configuracion devuelve 401.
                SecurityContextHolder.clearContext();
            }
        }

        chain.doFilter(request, response);
    }
}
