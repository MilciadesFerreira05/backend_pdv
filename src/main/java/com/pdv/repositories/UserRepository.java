package com.pdv.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pdv.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
