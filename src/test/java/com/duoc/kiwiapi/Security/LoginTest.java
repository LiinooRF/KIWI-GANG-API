package com.duoc.kiwiapi.Security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.duoc.kiwiapi.Model.UsuarioModel;
import com.duoc.kiwiapi.Service.UsuarioService;

//Prueba del login de punta a punta: busca el usuario, compara la clave con bcrypt y entrega el token.
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LoginTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UsuarioService usuarioService;

    @BeforeEach
    void crearUsuarioDePrueba() {
        UsuarioModel usuario = new UsuarioModel();
        usuario.setRun("111111111");
        usuario.setNombre("Rosario");
        usuario.setApellidos("Prueba");
        usuario.setCorreo("rosario@kiwi.cl");
        usuario.setContrasena("Clave-2026");
        usuario.setComuna("Santiago");
        usuario.setRol("administrador");
        //guardar encripta la clave, igual que cuando se registra un usuario de verdad.
        //si quedo de otra prueba anterior primero se borra.
        UsuarioModel anterior = usuarioService.obtenerPorCorreo("rosario@kiwi.cl");
        if (anterior != null) {
            usuarioService.eliminar(anterior.getId());
        }
        usuarioService.guardar(usuario);
    }

    @Test
    void loginConLaClaveCorrectaDevuelveElToken() throws Exception {
        mvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"correo\": \"rosario@kiwi.cl\", \"contrasena\": \"Clave-2026\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").isNotEmpty())
            .andExpect(jsonPath("$.rol").value("administrador"))
            .andExpect(jsonPath("$.nombre").value("Rosario"));
    }

    @Test
    void loginConLaClaveMalaDevuelve401() throws Exception {
        mvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"correo\": \"rosario@kiwi.cl\", \"contrasena\": \"otra-clave\"}"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void loginConUnCorreoQueNoExisteDevuelve401() throws Exception {
        mvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"correo\": " + "\"nadie@kiwi.cl\"" + ", \"contrasena\": \"Clave-2026\"}"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void elRegistroPublicoSiempreCreaClientes() throws Exception {
        //aunque el json mande otro rol, el registro publico lo ignora y crea un cliente
        mvc.perform(post("/api/v1/auth/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"run\":\"246813579\",\"nombre\":\"Nueva\",\"apellidos\":\"Persona\",\"correo\":\"nueva@kiwi.cl\",\"contrasena\":\"Clave-2026\",\"comuna\":\"Maipu\",\"rol\":\"administrador\"}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.rol").value("cliente"));
    }
}
