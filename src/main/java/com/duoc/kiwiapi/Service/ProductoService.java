package com.duoc.kiwiapi.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.duoc.kiwiapi.Model.ProductoModel;

@Service 
public class ProductoService {
    //Por hacer
    //Funciones sin hacer usadas en el primer controller: obtenerTodos(), obtenerPorId(id), guardar(producto), actualizar(id, producto), eliminar(id)
    public List<ProductoModel> obtenerTodos() {
        return new ArrayList<>();
    }

    public ProductoModel obtenerPorId(Long id) {
        return null;
    }

    public ProductoModel guardar(ProductoModel Producto) {
        return null;
    }

    public ProductoModel actualizar(Long id, ProductoModel Producto) {
        return null;
    }

    //void: DELETE no devuelve datos
    public void eliminar(Long id) {
        //vacío temporal
    }
}
