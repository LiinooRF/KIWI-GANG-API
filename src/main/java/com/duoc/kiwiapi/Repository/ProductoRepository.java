package com.duoc.kiwiapi.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.duoc.kiwiapi.Model.ProductoModel;

@Repository
public interface ProductoRepository extends JpaRepository<ProductoModel, Long> {

    // spring arma la consulta leyendo el nombre del metodo
    // findByCodigo es igual a WHERE codigo = ?
    Optional<ProductoModel> findByCodigo(String codigo);

    // CategoriaId entra a la relacion y compara el id de la categoria
    List<ProductoModel> findByCategoriaId(Long categoriaId);

    // trae solo los productos con en_oferta en true
    List<ProductoModel> findByEnOfertaTrue();

    // aca se comparan dos columnas del mismo producto y eso no se puede con el nombre
    // por eso la consulta va escrita a mano, con menor o igual como en el frontend
    @Query("SELECT p FROM ProductoModel p WHERE p.stock <= p.stockCritico")
    List<ProductoModel> buscarBajoStockCritico();
}