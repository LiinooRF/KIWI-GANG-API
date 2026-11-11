package com.duoc.kiwiapi.Service;
import java.util.List;

import org.springframework.stereotype.Service;

import java.util.ArrayList;

import com.duoc.kiwiapi.Model.OrdenModel;

@Service 
public class OrdenService {
    //Por hacer
    public List<OrdenModel> obtenerTodas() {
        return new ArrayList<>();
    }

    public OrdenModel obtenerPorId(Long id) {
        return null;
    }

    public OrdenModel crearOrden(OrdenModel orden) {
        // Generar código, calcular total y descontar stock
        return null;
    }
}
