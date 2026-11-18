package com.duoc.kiwiapi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.duoc.kiwiapi.Model.OrdenModel;

// por ahora basta con los metodos que vienen solos
@Repository
public interface OrdenRepository extends JpaRepository<OrdenModel, Long> {
}