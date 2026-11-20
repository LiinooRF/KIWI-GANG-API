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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    //Para guardar las claves encriptadas con bcrypt y no en texto plano.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //En la ec2 el front queda en el puerto 80 y la api en el 8080, para el navegador son
    //sitios distintos y sin esto bloquea las llamadas. Se deja abierto a cualquier origen
    //porque el login va con token en el header y no con cookies.
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain filtros(HttpSecurity http, JwtUtil jwtUtil) throws Exception {
        http
            //Con token no se usa csrf ni sesiones, cada peticion trae su token.
            .csrf(csrf -> csrf.disable())
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            //Usa la configuracion de cors de abajo para que react pueda llamar a la api.
            .cors(Customizer.withDefaults())
            .authorizeHttpRequests(rutas -> rutas
                //Abierto: login y swagger.
                .requestMatchers("/api/v1/auth/**").permitAll()
                //Spring redirige los errores a /error, si no esta abierto los 400 llegan como 403.
                .requestMatchers("/error").permitAll()
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
