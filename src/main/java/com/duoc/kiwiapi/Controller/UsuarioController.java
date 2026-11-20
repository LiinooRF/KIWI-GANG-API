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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.duoc.kiwiapi.Model.UsuarioModel;

@RestController 
@RequestMapping("/api/v1/usuarios")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Usuarios", description = "Gestión de usuarios")
public class UsuarioController {
    @Autowired 
    private UsuarioService usuarioService;

    @GetMapping 
    @Operation(
        summary = "Listar todos los usuarios registrados", 
        description = "Devuelve la lista completa de usuarios en sistema."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida con éxito"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<UsuarioModel>> listarUsuarios() {
        System.out.println("[UsuarioController] -> listarUsuarios");
        List<UsuarioModel> usuarios = usuarioService.obtenerTodos();
        return ResponseEntity.ok(usuarios); // 200 OK   
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar un usuario por su ID", 
        description = "Devuelve el detalle de un usuario especifico segun su identificador."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<UsuarioModel> buscarUsuario(@PathVariable Long id) {
        System.out.println("[UsuarioController] -> buscarUsuario");
        UsuarioModel usuario = usuarioService.obtenerPorId(id);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); //404, Not found.
        }
        return ResponseEntity.ok(usuario);
    }

    @PostMapping
    @Operation(
        summary = "Ingresar un nuevo usuario", 
        description = "Registra un nuevo usuario en el sistema y retorna su objeto correspondiente."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario registrado"),
        @ApiResponse(responseCode = "400", description = "Datos ingresados incompletos o incorrectos")
    })
    public ResponseEntity<UsuarioModel> crearUsuario(@Valid @RequestBody UsuarioModel usuario) {
        System.out.println("[UsuarioController] -> crearUsuario");
        UsuarioModel nuevoUsuario = usuarioService.guardar(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario); //201 Created.
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Editar un usuario existente", 
        description = "Edita un usuario que ya está en el sistema y retorna su objeto modificado."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario editado"),
        @ApiResponse(responseCode = "400", description = "Datos ingresados incompletos o incorrectos"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<UsuarioModel> actualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioModel usuario) {
        System.out.println("[UsuarioController] -> actualizarUsuario");
        UsuarioModel usuarioExistente = usuarioService.obtenerPorId(id);
        if (usuarioExistente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); //404 Not found.
        }
        UsuarioModel usuarioActualizado = usuarioService.actualizar(id, usuario);
        return ResponseEntity.ok(usuarioActualizado); 
    }
    
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Elimina un usuario", 
        description = "Elimina un usuario del sistema y retorna un objeto vacío."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuario Eliminado"),
        @ApiResponse(responseCode = "404", description = "El usuario a eliminar no esta registrado.")
    })
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
