package com.duoc.kiwiapi.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.duoc.kiwiapi.Model.CategoriaModel;
import com.duoc.kiwiapi.Model.ProductoModel;
import com.duoc.kiwiapi.Repository.ProductoRepository;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    // se usa para revisar que la categoria del producto exista
    @Autowired
    private CategoriaService categoriaService;

    public List<ProductoModel> obtenerTodos() {
        return productoRepository.findAll();
    }

    public ProductoModel obtenerPorId(Long id) {
        return productoRepository.findById(id).orElse(null);
    }

    public ProductoModel guardar(ProductoModel producto) {
        // no se puede repetir el codigo
        if (productoRepository.findByCodigo(producto.getCodigo()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un producto con ese codigo");
        }
        producto.setCategoria(buscarCategoria(producto));
        return productoRepository.save(producto);
    }

    public ProductoModel actualizar(Long id, ProductoModel producto) {
        // el codigo puede quedar igual, pero no puede ser el de otro producto
        ProductoModel conMismoCodigo = productoRepository.findByCodigo(producto.getCodigo()).orElse(null);
        if (conMismoCodigo != null && !conMismoCodigo.getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un producto con ese codigo");
        }
        producto.setCategoria(buscarCategoria(producto));
        // al tener id, save actualiza en vez de crear uno nuevo
        producto.setId(id);
        return productoRepository.save(producto);
    }

    public void eliminar(Long id) {
        productoRepository.deleteById(id);
    }

    // consultas extra que pide la issue, quedan listas para usarlas en los endpoints

    public ProductoModel obtenerPorCodigo(String codigo) {
        return productoRepository.findByCodigo(codigo).orElse(null);
    }

    public List<ProductoModel> obtenerPorCategoria(Long categoriaId) {
        return productoRepository.findByCategoriaId(categoriaId);
    }

    public List<ProductoModel> obtenerEnOferta() {
        return productoRepository.findByEnOfertaTrue();
    }

    public List<ProductoModel> obtenerBajoStockCritico() {
        return productoRepository.buscarBajoStockCritico();
    }

    // revisa que el producto traiga una categoria y que exista en la base
    // devuelve la categoria completa para que el json de respuesta venga con su nombre
    private CategoriaModel buscarCategoria(ProductoModel producto) {
        if (producto.getCategoria() == null || producto.getCategoria().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El producto debe tener una categoria");
        }
        CategoriaModel categoria = categoriaService.obtenerPorId(producto.getCategoria().getId());
        if (categoria == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La categoria no existe");
        }
        return categoria;
    }
}