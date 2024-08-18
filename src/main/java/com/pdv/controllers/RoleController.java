package com.pdv.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.pdv.models.Role;
import com.pdv.repositories.RoleRepository;
import com.pdv.services.RoleService;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
@Validated
public class RoleController {


    private RoleRepository roleRepository;

    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    

    @GetMapping("/all")
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    // Obtener un rol por id
    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
        Optional<Role> role = roleRepository.findById(id);
        return role.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Crear un nuevo rol
    @PostMapping
    public ResponseEntity<Role> createRole(@Valid @RequestBody Role role) {
        Role createdRole = roleRepository.save(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRole);
    }

    // Actualizar un rol existente
    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable Long id, @Valid @RequestBody Role role) {
        Optional<Role> existingRole = roleRepository.findById(id);
        if (existingRole.isPresent()) {
            role.setId(id);
            Role updatedRole = roleRepository.save(role);
            return ResponseEntity.ok(updatedRole);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Eliminar un rol
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        if (roleRepository.findById(id).isPresent()) {
            roleRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
