package com.duoc.kiwiapi.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    //Para guardar las claves encriptadas con bcrypt y no en texto plano.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filtros(HttpSecurity http, JwtUtil jwtUtil) throws Exception {
        http
            //Con token no se usa csrf ni sesiones, cada peticion trae su token.
            .csrf(csrf -> csrf.disable())
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            //Usa los @CrossOrigin de los controllers para que react pueda llamar a la api.
            .cors(Customizer.withDefaults())
            .authorizeHttpRequests(rutas -> rutas
                //Abierto: login y swagger.
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
                //La tienda ve productos y categorias sin login.
                .requestMatchers(HttpMethod.GET, "/api/v1/productos/**", "/api/v1/categorias/**").permitAll()
                //El vendedor solo ve las ordenes, el admin todo.
                .requestMatchers(HttpMethod.GET, "/api/v1/ordenes/**").hasAnyRole("ADMINISTRADOR", "VENDEDOR")
                //Cualquier usuario con sesion puede hacer una compra.
                .requestMatchers(HttpMethod.POST, "/api/v1/ordenes").authenticated()
                //Todo lo demas (crear, editar, borrar, usuarios) solo el administrador.
                .anyRequest().hasRole("ADMINISTRADOR")
            )
            //Sin token o con token malo devuelve 401 en vez de la pagina de login de spring.
            .exceptionHandling(e -> e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
            .httpBasic(b -> b.disable())
            .formLogin(f -> f.disable())
            //Nuestro filtro va antes del filtro de login normal de spring.
            .addFilterBefore(new JwtFiltro(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
