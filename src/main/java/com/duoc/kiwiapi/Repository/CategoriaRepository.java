package com.duoc.kiwiapi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.duoc.kiwiapi.Model.CategoriaModel;

// al extender JpaRepository spring crea solo los metodos para guardar, buscar y borrar
// CategoriaModel es la entidad y Long es el tipo del id
@Repository
public interface CategoriaRepository extends JpaRepository<CategoriaModel, Long> {
}