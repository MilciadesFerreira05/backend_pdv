package com.pdv.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pdv.models.Client;


@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findByDeletedAtIsNull();
}
