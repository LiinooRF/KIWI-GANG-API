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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController 
//Mapping para todos los endpoints.
@RequestMapping("/api/v1/productos")
@CrossOrigin(origins = "http://localhost:3000") //Cross para React.
@Tag(name = "Productos", description = "Gestión de productos")
public class ProductoController {
    //Autowired inyecta dependencias simplificando su instanciamiento.
    @Autowired 
    private ProductoService productoService;

    //Mapping para el get de todos los productos.
    @GetMapping 
    @Operation(
        summary = "Listar todos los productos", 
        description = "Devuelve la lista completa de productos."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida con éxito"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
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
    @Operation(
        summary = "Buscar un producto por ID", 
        description = "Devuelve el detalle de un producto especifico segun su identificador."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto encontrado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
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
    @Operation(
        summary = "Crear un nuevo producto", 
        description = "Registra un nuevo producto en el sistema y retorna su objeto correspondiente."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Producto añadido"),
        @ApiResponse(responseCode = "400", description = "Datos ingresados incompletos o incorrectos")
    })
    public ResponseEntity<ProductoModel> crearProducto(@Valid @RequestBody ProductoModel producto) {
        System.out.println("[ProductoController] -> crearProducto");
        ProductoModel nuevoProducto = productoService.guardar(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto); //201 Created.
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Editar un producto existente", 
        description = "Edita un producto que ya está en el sistema y retorna su objeto modificado."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto editado"),
        @ApiResponse(responseCode = "400", description = "Datos ingresados incompletos o incorrectos"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
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
    @Operation(
        summary = "Elimina un producto", 
        description = "Elimina un producto del sistema y retorna un objeto vacío."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Producto Eliminado"),
        @ApiResponse(responseCode = "404", description = "El producto a eliminar no existe.")
    })
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