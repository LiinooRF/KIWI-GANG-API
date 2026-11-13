package com.duoc.kiwiapi.Security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

//Clase que crea y lee los token jwt.
@Component
public class JwtUtil {

    //El token dura una hora (en milisegundos).
    private static final long DURACION = 1000 * 60 * 60;

    private final SecretKey clave;

    //La clave viene del application.properties, que la saca de la variable JWT_SECRET.
    public JwtUtil(@Value("${jwt.secreto}") String secreto) {
        //Si la clave tiene menos de 32 caracteres esto tira error al partir la app.
        this.clave = Keys.hmacShaKeyFor(secreto.getBytes(StandardCharsets.UTF_8));
    }

    //Crea el token con el correo y el rol del usuario adentro.
    public String generarToken(String correo, String rol) {
        Date ahora = new Date();
        return Jwts.builder()
                .subject(correo)
                .claim("rol", rol)
                .issuedAt(ahora)
                .expiration(new Date(ahora.getTime() + DURACION))
                .signWith(clave)
                .compact();
    }

    //Lee el token y devuelve los datos de adentro.
    //Si esta vencido, mal firmado o mal escrito tira una JwtException.
    public Claims leerToken(String token) {
        return Jwts.parser()
                .verifyWith(clave)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
