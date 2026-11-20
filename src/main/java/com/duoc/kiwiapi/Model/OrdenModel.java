package com.duoc.kiwiapi.Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ordenes")
@Data 
@NoArgsConstructor
@AllArgsConstructor 
public class OrdenModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // codigo para mostrar al cliente, lo genera el service
    @Column(unique = true)
    private String codigo;

    private LocalDateTime fecha;

    // suma de los subtotales, lo calcula el service
    private Integer total;

    // muchas ordenes pueden ser de un mismo usuario
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private UsuarioModel usuario;

    // una orden tiene muchos detalles, uno por cada producto
    // mappedBy dice que la columna orden_id esta en la tabla de detalles
    // cascade hace que al guardar la orden se guarden sus detalles tambien
    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL)
    private List<DetalleOrdenModel> detalles = new ArrayList<>();
}