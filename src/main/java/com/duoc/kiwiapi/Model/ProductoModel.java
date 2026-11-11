package com.duoc.kiwiapi.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity 
@Table (name = "productos")
@Data 
@NoArgsConstructor 
@AllArgsConstructor 
public class ProductoModel {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true) //unique evita que se repita el codigo del producto
    private String codigo;

    private String nombre;
    
    @Column(length = 500) //descripcion maxima de 500 caracteres
    private String descripcion;

    private Integer precio;

    private Integer stock;

    private Integer stockCritico;

    private String imagen;

    private boolean enOferta;

    private Integer precioOferta;

    @ManyToOne // muchos productos pueden pertenecer a una categoria
    @JoinColumn (name = "categoria_id") // en la tabla se crea categoria_id como llave foranea q apunta a categoria
    private CategoriaModel categoria; 

}
