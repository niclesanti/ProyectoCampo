package com.campito.backend.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.campito.backend.model.CuentaBancaria;

public interface CuentaBancariaRepository extends JpaRepository<CuentaBancaria, Long> {
    List<CuentaBancaria> findByEspacioTrabajo_Id(Long idEspacioTrabajo);
}
