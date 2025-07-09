package com.campito.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.campito.backend.model.ContactoTransferencia;

import java.util.List;

public interface ContactoTransferenciaRepository extends JpaRepository<ContactoTransferencia, Long> {
    List<ContactoTransferencia> findByEspacioTrabajo_Id(Long idEspacioTrabajo);
}
