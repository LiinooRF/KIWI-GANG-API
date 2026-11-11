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

@RestController 
@RequestMapping ("/api/v1/ordenes")
@CrossOrigin(origins = "http://localhost:3000")
public class OrdenController {
    @Autowired
    private OrdenService ordenService;

    @GetMapping
    public ResponseEntity<List<OrdenModel>> listarOrdenes() {
        System.out.println("[OrdenController] -> listarOrdenes");
        List<OrdenModel> ordenes = ordenService.obtenerTodas();
        return ResponseEntity.ok(ordenes); // 200 OK
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdenModel> buscarOrden(@PathVariable Long id) {
        System.out.println("[OrdenController] -> buscarOrden");
        OrdenModel orden = ordenService.obtenerPorId(id);
        return ResponseEntity.ok(orden); // 200 OK
    }

    @PostMapping 
    public ResponseEntity<OrdenModel> crearOrden(@RequestBody OrdenModel orden) {
        System.out.println("[OrdenController] -> crearOrden");
        OrdenModel nuevaOrden = ordenService.crearOrden(orden);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaOrden); // 201 Created
    }
}