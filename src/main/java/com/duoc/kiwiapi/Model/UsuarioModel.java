package com.duoc.kiwiapi.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity 
@Table (name = "usuarios")
@Data 
@NoArgsConstructor 
@AllArgsConstructor 
public class UsuarioModel {
    
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true) //unique evita que se repita el run del usuario 
    private String run;

    private String nombre;

    private String apellidos;

    @Column (unique = true) //unique evita que se repita el correo del usuario
    private String correo;

    private String contrasena;

    private String comuna;

    private String rol; //por ahora texto simple (cliente,admin,operario)
    
}