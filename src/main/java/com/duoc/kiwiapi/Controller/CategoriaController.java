package com.duoc.kiwiapi.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

//Importes sin base creada. (Nombres de files puestos por el momento.)
import com.duoc.kiwiapi.Model.CategoriaModel;
//la funcion usada desde Service es un nombre teorico tambien.
import com.duoc.kiwiapi.Service.CategoriaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController 
@RequestMapping("/api/v1/categorias")
@CrossOrigin(origins = "http://localhost:3000") //Cross para React.
//Define los detalles del apartado del controlador en la api.
@Tag(name = "Categorías", description = "Gestión de categorías [Fragil / No Fragil]")
public class CategoriaController {
    @Autowired 
    private CategoriaService categoriaService;

    @GetMapping 
    //Define la accion de la operacion a realizar.
    @Operation(
        summary = "Listar todas las categorías", 
        description = "Obtiene la lista de las categorías disponibles."
    )
    //Se definen los tipos de respuesta que entrega la api.
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida con éxito"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<CategoriaModel>> listarCategorias() {
        System.out.println("[CategoriaController] -> listarCategorias");
        List<CategoriaModel> categorias = categoriaService.obtenerTodas();
        return ResponseEntity.ok(categorias); //200 OK.
    }
}
