package com.duoc.kiwiapi.Service;
import java.util.List;

import org.springframework.stereotype.Service;

import java.util.ArrayList;

import com.duoc.kiwiapi.Model.UsuarioModel;

@Service
public class UsuarioService {
    //Por hacer
    public List<UsuarioModel> obtenerTodos() {
        return new ArrayList<>();
    }

    public UsuarioModel obtenerPorId(Long id) {
        return null;
    }

    public UsuarioModel guardar(UsuarioModel usuario) {
        return null;
    }

    public UsuarioModel actualizar(Long id, UsuarioModel usuario) {
        return null;
    }

    //void: DELETE no devuelve datos
    public void eliminar(Long id) {
        //vacío temporal
    }
}
