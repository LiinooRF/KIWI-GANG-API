package com.duoc.kiwiapi.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.duoc.kiwiapi.Model.UsuarioModel;
import com.duoc.kiwiapi.Security.JwtUtil;
import com.duoc.kiwiapi.Service.UsuarioService;

//Endpoint del login. Recibe correo y clave, revisa que esten bien y devuelve el token.
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UsuarioService usuarioService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UsuarioService usuarioService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    //El record es una clase chica que solo guarda datos, java le arma solo los getters.
    record DatosLogin(String correo, String contrasena) {}

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody DatosLogin datos) {
        //Se busca al usuario por el correo.
        UsuarioModel usuario = usuarioService.obtenerPorCorreo(datos.correo());

        //matches compara la clave que llego con la encriptada que esta en la base.
        if (usuario == null || !passwordEncoder.matches(datos.contrasena(), usuario.getContrasena())) {
            //No se avisa si falto el correo o la clave, para no darle pistas a nadie.
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("correo o clave incorrectos");
        }

        //Si esta todo bien se crea el token y se manda junto al nombre y el rol.
        String token = jwtUtil.generarToken(usuario.getCorreo(), usuario.getRol());
        return ResponseEntity.ok(new RespuestaLogin(token, usuario.getNombre(), usuario.getCorreo(), usuario.getRol()));
    }

    //Registro publico: cualquiera puede crear su cuenta, pero siempre como cliente,
    //el rol que venga en el json se ignora. Los usuarios con otros roles los crea el admin.
    @PostMapping("/registro")
    public ResponseEntity<?> registro(@RequestBody UsuarioModel usuario) {
        usuario.setRol("cliente");
        UsuarioModel nuevo = usuarioService.guardar(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    //Lo que devuelve el login. El front guarda el token y el rol lo usa para las vistas.
    record RespuestaLogin(String token, String nombre, String correo, String rol) {}
}
