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
//la funcion usada desde Service es un nombre teorico tambien. (En rojo)
import com.duoc.kiwiapi.Service.CategoriaService;

@RestController 
@RequestMapping("/api/v1/categorias")
@CrossOrigin(origins = "http://localhost:3000") //Cross para React.
public class CategoriaController {
    @Autowired 
    private CategoriaService categoriaService;

    @GetMapping 
    public ResponseEntity<List<CategoriaModel>> listarCategorias() {
        System.out.println("[CategoriaController] -> listarCategorias");
        List<CategoriaModel> categorias = categoriaService.obtenerTodas();
        return ResponseEntity.ok(categorias); //200 OK.
    }
}
