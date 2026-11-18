package com.duoc.kiwiapi.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.duoc.kiwiapi.Model.CategoriaModel;
import com.duoc.kiwiapi.Repository.CategoriaRepository;

@Service
public class CategoriaService {

    // spring entrega el repositorio solo, no hay que hacer new
    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<CategoriaModel> obtenerTodas() {
        return categoriaRepository.findAll();
    }

    // si no existe devuelve null y el controller responde 404
    public CategoriaModel obtenerPorId(Long id) {
        return categoriaRepository.findById(id).orElse(null);
    }
}