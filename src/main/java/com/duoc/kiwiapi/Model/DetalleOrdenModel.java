package com.duoc.kiwiapi.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "detalles_orden")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleOrdenModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // la orden a la que pertenece este detalle
    // JsonIgnore evita un ciclo infinito al convertir a json (orden, detalle, orden...)
    // los Exclude evitan el mismo ciclo en los metodos que genera lombok
    @ManyToOne
    @JoinColumn(name = "orden_id")
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private OrdenModel orden;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private ProductoModel producto;

    private Integer cantidad;

    // precio al momento de comprar, asi la orden no cambia si despues cambia el precio del producto
    private Integer precioUnitario;

    // precioUnitario por cantidad
    private Integer subtotal;
}