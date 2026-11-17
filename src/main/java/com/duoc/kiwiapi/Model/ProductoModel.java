package com.duoc.kiwiapi.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;
    
    @Column(length = 500) //descripcion maxima de 500 caracteres
    private String descripcion;

    @Min(value = 1, message = "El precio debe ser mayor a 0")
    private Integer precio;

    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    private Integer stockCritico;

    private String imagen;

    private boolean enOferta;

    private Integer precioOferta;

    @ManyToOne // muchos productos pueden pertenecer a una categoria
    @JoinColumn (name = "categoria_id") // en la tabla se crea categoria_id como llave foranea q apunta a categoria
    private CategoriaModel categoria; 

}
