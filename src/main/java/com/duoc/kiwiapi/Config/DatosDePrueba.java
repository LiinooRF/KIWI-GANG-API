package com.duoc.kiwiapi.Config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.duoc.kiwiapi.Model.UsuarioModel;
import com.duoc.kiwiapi.Service.UsuarioService;

//Carga los usuarios de prueba cuando la base esta vacia.
//Sirve para poder probar el login apenas se levanta la api, porque la base parte vacia.
@Configuration
public class DatosDePrueba {

    @Bean
    CommandLineRunner cargarUsuarios(UsuarioService usuarioService) {
        return args -> {
            if (usuarioService.obtenerTodos().isEmpty()) {
                //Son los mismos que muestra la pagina de login del front.
                crear(usuarioService, "112223334", "Rodrigo", "Fuentes", "rodrigofuentes@duoc.cl", "admin123", "Santiago", "administrador");
                crear(usuarioService, "445556667", "Javiera", "Diaz", "javieradiaz@duoc.cl", "oper123", "Nuñoa", "vendedor");
                crear(usuarioService, "123456789", "Catalina", "Soto", "catalinasoto@gmail.com", "ctr123", "Providencia", "cliente");
            }
        };
    }

    private void crear(UsuarioService servicio, String run, String nombre, String apellidos,
                       String correo, String contrasena, String comuna, String rol) {
        UsuarioModel usuario = new UsuarioModel();
        usuario.setRun(run);
        usuario.setNombre(nombre);
        usuario.setApellidos(apellidos);
        usuario.setCorreo(correo);
        usuario.setContrasena(contrasena);
        usuario.setComuna(comuna);
        usuario.setRol(rol);
        //guardar encripta la clave con bcrypt
        servicio.guardar(usuario);
    }
}
