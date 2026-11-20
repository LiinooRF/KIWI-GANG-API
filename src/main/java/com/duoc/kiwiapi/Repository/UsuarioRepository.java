package com.duoc.kiwiapi.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.duoc.kiwiapi.Model.UsuarioModel;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {

    // se usa en el login para encontrar al usuario por su correo
    Optional<UsuarioModel> findByCorreo(String correo);

    // devuelve true si ya hay un usuario con ese correo
    // sirve para no dejar registrar dos veces el mismo
    boolean existsByCorreo(String correo);
}