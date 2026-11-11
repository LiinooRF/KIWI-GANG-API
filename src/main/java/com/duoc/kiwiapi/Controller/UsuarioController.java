package com.duoc.kiwiapi.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import jakarta.validation.Valid;

import com.duoc.kiwiapi.Service.UsuarioService;
import com.duoc.kiwiapi.Model.UsuarioModel;

@RestController 
@RequestMapping("/api/v1/usuarios")
@CrossOrigin(origins = "http://localhost:3000")
public class UsuarioController {
    @Autowired 
    private UsuarioService usuarioService;

    @GetMapping 
    public ResponseEntity<List<UsuarioModel>> listarUsuarios() {
        System.out.println("[UsuarioController] -> listarUsuarios");
        List<UsuarioModel> usuarios = usuarioService.obtenerTodos();
        return ResponseEntity.ok(usuarios); // 200 OK   
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioModel> buscarUsuario(@PathVariable Long id) {
        System.out.println("[UsuarioController] -> buscarUsuario");
        UsuarioModel usuario = usuarioService.obtenerPorId(id);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); //404, Not found.
        }
        return ResponseEntity.ok(usuario);
    }

    @PostMapping
    public ResponseEntity<UsuarioModel> crearUsuario(@Valid @RequestBody UsuarioModel usuario) {
        System.out.println("[UsuarioController] -> crearUsuario");
        UsuarioModel nuevoUsuario = usuarioService.guardar(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario); //201 Created.
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioModel> actualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioModel usuario) {
        System.out.println("[UsuarioController] -> actualizarUsuario");
        UsuarioModel usuarioExistente = usuarioService.obtenerPorId(id);
        if (usuarioExistente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); //404 Not found.
        }
        UsuarioModel usuarioActualizado = usuarioService.actualizar(id, usuario);
        return ResponseEntity.ok(usuarioActualizado); 
    }
    
    @DeleteMapping 
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        System.out.println("[UsuarioController] -> eliminarUsuario");
        UsuarioModel usuarioExistente = usuarioService.obtenerPorId(id);
        if (usuarioExistente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); //404 Not found.
        }
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build(); 
    }
}
