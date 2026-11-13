package com.duoc.kiwiapi.Security;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

//Pruebas para ver que las rutas quedan protegidas segun el token.
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SeguridadTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void sinTokenDevuelve401() throws Exception {
        mvc.perform(get("/api/v1/usuarios"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void tokenFalsoDevuelve401() throws Exception {
        mvc.perform(get("/api/v1/usuarios").header("Authorization", "Bearer token-inventado"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void vendedorNoPuedeVerUsuarios() throws Exception {
        String token = jwtUtil.generarToken("vendedor@kiwi.cl", "vendedor");
        mvc.perform(get("/api/v1/usuarios").header("Authorization", "Bearer " + token))
            .andExpect(status().isForbidden());
    }

    @Test
    void productosSonPublicos() throws Exception {
        //Sin token igual pasa la seguridad (no importa si da 200 o 404, solo que no sea 401).
        mvc.perform(get("/api/v1/productos"))
            .andExpect(r -> assertNotEquals(401, r.getResponse().getStatus()));
    }
}
