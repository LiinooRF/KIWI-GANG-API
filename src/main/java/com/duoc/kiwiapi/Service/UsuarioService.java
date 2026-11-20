package com.duoc.kiwiapi.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.duoc.kiwiapi.Model.UsuarioModel;
import com.duoc.kiwiapi.Repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // viene del SecurityConfig de lino, encripta con bcrypt
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UsuarioModel> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    public UsuarioModel obtenerPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    // la va a usar el login para buscar al usuario
    public UsuarioModel obtenerPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo).orElse(null);
    }

    public UsuarioModel guardar(UsuarioModel usuario) {
        // regla de negocio, no puede haber dos usuarios con el mismo correo
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un usuario con ese correo");
        }
        // la clave se guarda encriptada, nunca en texto plano
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        // si no viene rol se registra como cliente
        if (usuario.getRol() == null || usuario.getRol().isBlank()) {
            usuario.setRol("cliente");
        }
        return usuarioRepository.save(usuario);
    }

    public UsuarioModel actualizar(Long id, UsuarioModel usuario) {
        UsuarioModel existente = obtenerPorId(id);
        if (existente == null) {
            return null;
        }
        // el correo puede quedar igual, pero no puede ser el de otro usuario
        UsuarioModel conMismoCorreo = usuarioRepository.findByCorreo(usuario.getCorreo()).orElse(null);
        if (conMismoCorreo != null && !conMismoCorreo.getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un usuario con ese correo");
        }
        // si no mandan clave nueva se mantiene la que tenia, que ya esta encriptada
        if (usuario.getContrasena() == null || usuario.getContrasena().isBlank()) {
            usuario.setContrasena(existente.getContrasena());
        } else {
            usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        }
        usuario.setId(id);
        return usuarioRepository.save(usuario);
    }

    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }
}