package com.duoc.kiwiapi.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

import com.duoc.kiwiapi.Model.OrdenModel;
import com.duoc.kiwiapi.Service.OrdenService;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController 
@RequestMapping ("/api/v1/ordenes")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Órdenes", description = "Gestión de órdenes")
public class OrdenController {
    @Autowired
    private OrdenService ordenService;

    @GetMapping
    @Operation(
        summary = "Listar todas las órdenes", 
        description = "Devuelve la lista completa de órdenes."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida con éxito"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<OrdenModel>> listarOrdenes() {
        System.out.println("[OrdenController] -> listarOrdenes");
        List<OrdenModel> ordenes = ordenService.obtenerTodas();
        return ResponseEntity.ok(ordenes); // 200 OK
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar una orden por ID", 
        description = "Devuelve el detalle de una orden especifica segun su identificador."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Orden encontrada"),
        @ApiResponse(responseCode = "404", description = "Orden no encontrada")
    })
    public ResponseEntity<OrdenModel> buscarOrden(@PathVariable Long id) {
        System.out.println("[OrdenController] -> buscarOrden");
        OrdenModel orden = ordenService.obtenerPorId(id);
        return ResponseEntity.ok(orden); // 200 OK
    }

    @PostMapping
        @Operation(
        summary = "Crear una nueva orden", 
        description = "Registra una nueva orden en el sistema y retorna su objeto correspondiente."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Orden añadida"),
        @ApiResponse(responseCode = "400", description = "Datos ingresados incompletos o incorrectos")
    })
    public ResponseEntity<OrdenModel> crearOrden(@RequestBody OrdenModel orden) {
        System.out.println("[OrdenController] -> crearOrden");
        OrdenModel nuevaOrden = ordenService.guardar(orden);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaOrden); // 201 Created
    }
}