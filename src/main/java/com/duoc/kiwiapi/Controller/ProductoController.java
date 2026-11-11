package com.duoc.kiwiapi.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin; //Cors
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
//Importes sin base creada. (Nombres de files puestos por el momento.)
import com.duoc.kiwiapi.Model.ProductoModel;
//Todas las funciones usadas desde Service son nombres teoricos tambien.
import com.duoc.kiwiapi.Service.ProductoService;

@RestController 
//Mapping para todos los endpoints.
@RequestMapping("/api/v1/productos")
@CrossOrigin(origins = "http://localhost:3000") //Cross para React.
public class ProductoController {
    //Autowired inyecta dependencias simplificando su instanciamiento.
    @Autowired 
    private ProductoService productoService;

    //Mapping para el get de todos los productos.
    @GetMapping 
    //ResponseEntity contiene el call y define el tipo de estado HTTP.
    public ResponseEntity<List<ProductoModel>> listarProductos() {
        //Mensaje en terminal para comprobar que el endpoint responde.
        System.out.println("[ProductoController] -> listarProductos");
        //Se llama al Service.
        List<ProductoModel> productos = productoService.obtenerTodos();
        return ResponseEntity.ok(productos); //200 OK.
    }

    //Mapping para id especifico (se suma a la ruta principal)
    @GetMapping("/{id}")
    public ResponseEntity<ProductoModel> buscarProducto(@PathVariable Long id) {
        System.out.println("[ProductoController] -> buscarProducto id=" + id);
        //Se llama al Service
        ProductoModel producto = productoService.obtenerPorId(id);
        if (producto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); //404, Not found.
        }
        return ResponseEntity.ok(producto);
    }

    @PostMapping //Por ahora, el propio valid devuelve 400.
    public ResponseEntity<ProductoModel> crearProducto(@Valid @RequestBody ProductoModel producto) {
        System.out.println("[ProductoController] -> crearProducto");
        ProductoModel nuevoProducto = productoService.guardar(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto); //201 Created.
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoModel> actualizarProducto(@PathVariable Long id, @Valid @RequestBody ProductoModel producto) {
        System.out.println("[ProductoController] -> actualizarProducto");
        ProductoModel productoExistente = productoService.obtenerPorId(id);
        if (productoExistente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); //404 Not found.
        }
        ProductoModel productoActualizado = productoService.actualizar(id, producto);
        return ResponseEntity.ok(productoActualizado); //200 OK.
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
         System.out.println("[ProductoController] -> eliminarProducto");
        ProductoModel productoExistente = productoService.obtenerPorId(id);
        if (productoExistente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); //404 Not found.
        }
        //Si existe, se ejecuta su eliminacion desde el service.
        productoService.eliminar(id);
        return ResponseEntity.noContent().build(); //204 No content. (Para un delete exitoso.)
    }
}